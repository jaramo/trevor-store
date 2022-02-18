package io.trevor.store.number.service

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.trevor.store.fixture.Generators.randomInt
import io.trevor.store.fixture.Generators.uuid
import io.trevor.store.number.dto.ResponseNumberDto
import io.trevor.store.number.repository.NumberRepository
import io.trevor.store.number.repository.entity.NumberEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import java.util.UUID

@ExtendWith(MockKExtension::class)
class NumberServiceTest {

    @Test
    fun getAll(@MockK repo: NumberRepository) {
        every { repo.findAll() } returns (1..10).map { NumberEntity.create(it) }

        val result = NumberService(repo).getAll();
        assertThat(result).isNotNull
        assertThat(result).hasSize(10)

        verify { repo.findAll() }
        confirmVerified(repo)
    }

    @Test
    fun `number not found should return null`(@MockK repo: NumberRepository) {
        val id = UUID.randomUUID().toString()

        every { repo.findByIdOrNull(eq(id)) } returns null

        val result = NumberService(repo).get(id)
        assertThat(result).isNull()

        verify { repo.findByIdOrNull(id) }
        confirmVerified(repo)
    }

    @Test
    fun `number is found and should return dto`(@MockK repo: NumberRepository) {
        val id = uuid()
        val value = randomInt()

        every { repo.findByIdOrNull(eq(id)) } returns NumberEntity(id, value)

        val result = NumberService(repo).get(id)
        assertThat(result).isNotNull
        assertThat(result).isEqualTo(ResponseNumberDto(id, value))

        verify { repo.findByIdOrNull(id) }
        confirmVerified(repo)
    }

    @Test
    fun `store is success`(@MockK repo: NumberRepository) {
        val value = randomInt()

        every { repo.save(any()) } returnsArgument 0

        val result = NumberService(repo).store(value)
        assertThat(result).isNotNull
        assertThat(result?.id).isNotBlank
        assertThat(result?.value).isEqualTo(value)

        verify { repo.save(any()) }
        confirmVerified(repo)
    }

    @Test
    fun `store failed`(@MockK repo: NumberRepository) {
        val value = randomInt()

        every { repo.save(any()) } throws RuntimeException()

        val result = NumberService(repo).store(value)
        assertThat(result).isNull()

        verify { repo.save(any()) }
        confirmVerified(repo)
    }
}
