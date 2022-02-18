package io.trevor.store.number.controller;

import io.trevor.store.number.dto.CreateNumberDto
import io.trevor.store.number.dto.ResponseNumberDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NumberControllerTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `numbers returns empty list`() {
        val entity = restTemplate.getForEntity<List<ResponseNumberDto>>("/numbers")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEmpty()
    }

    @Test
    fun `POST to number returns bad request when empty body`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity("{}", headers)
        val response = restTemplate.postForEntity<String>("/numbers", request)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `number with is returned by id after create it`() {
        val value = randomInt()
        val request = CreateNumberDto(value)
        val postResponse = restTemplate.postForEntity<String>("/numbers", request)

        assertThat(postResponse.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(postResponse.body).isNotBlank

        postResponse.body?.let { id ->
            restTemplate.getForEntity<ResponseNumberDto>("/numbers/{0}", id).also { getResponse ->
                assertThat(getResponse).isNotNull
                assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
                assertThat(getResponse.body).isEqualTo(ResponseNumberDto(id, value))
            }
        } ?: fail { "I don't know how do we get here" }

    }

    @Test
    fun `GET all numbers should return a list of 10 elements`() {
        val r =
            (1..10)
                .map {  CreateNumberDto(randomInt()) }
                .map { restTemplate.postForEntity<String>("/numbers", it) }
                .map { it.body }

        assertThat(r).hasSize(10)

        val entity = restTemplate.getForEntity<List<ResponseNumberDto>>("/numbers")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).hasSize(10)
    }

    private fun randomInt(): Int = (Int.MIN_VALUE..Int.MAX_VALUE).random()

}
