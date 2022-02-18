package io.trevor.store.number.service

import io.trevor.store.number.dto.ResponseNumberDto
import io.trevor.store.number.repository.NumberRepository
import io.trevor.store.number.repository.entity.NumberEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class NumberService(private val repo: NumberRepository) {

    fun getAll(): List<ResponseNumberDto> =
        repo.findAll().toList().map { ResponseNumberDto.fromEntity(it) }

    fun get(id: String): ResponseNumberDto? =
        repo.findByIdOrNull(id)?.let { ResponseNumberDto.fromEntity(it) }

    fun store(value: Number): ResponseNumberDto? =
        try { repo.save(NumberEntity.create(value.toInt())).let { ResponseNumberDto.fromEntity(it) } }
        catch (e: Throwable) { null }

}

