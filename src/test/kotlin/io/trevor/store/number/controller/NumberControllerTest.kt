package io.trevor.store.number.controller;

import io.trevor.store.fixture.Generators.randomInt
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
        val response = restTemplate.getForEntity<List<ResponseNumberDto>>("/numbers")
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEmpty()
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
        val postResponse = restTemplate.postForEntity<ResponseNumberDto>("/numbers", request)

        assertThat(postResponse.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(postResponse.body).isNotNull

        postResponse.body?.let { data ->
            restTemplate.getForEntity<ResponseNumberDto>("/numbers/{0}", data.id).also { getResponse ->
                assertThat(getResponse).isNotNull
                assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
                assertThat(getResponse.body).isEqualTo(ResponseNumberDto(data.id, value))
            }
        } ?: fail { "I don't know how do we get here" }

    }

    @Test
    fun `GET all numbers should return a list of 10 elements`() {
        val r =
            (1..10)
                .map {  CreateNumberDto(randomInt()) }
                .map { restTemplate.postForEntity<ResponseNumberDto>("/numbers", it) }
                .map { it.body }

        assertThat(r).hasSize(10)

        val response = restTemplate.getForEntity<List<ResponseNumberDto>>("/numbers")
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).hasSize(10)
    }

}
