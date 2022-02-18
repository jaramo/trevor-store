package io.trevor.store.number.controller

import io.trevor.store.number.dto.CreateNumberDto
import io.trevor.store.number.dto.ResponseNumberDto
import io.trevor.store.number.repository.NumberRepository
import io.trevor.store.number.repository.entity.NumberEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid

@RestController
@RequestMapping("/numbers")
class NumberController(private val repo: NumberRepository) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(): List<ResponseNumberDto> {
        return repo.findAll().toList().map { ResponseNumberDto.fromEntity(it) }
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateNumberDto): String {
        return repo.save(NumberEntity.create(request.value)).id ?: throw RuntimeException("a mamarla")
    }

    @GetMapping(path = ["/{id}"])
    fun getNumber(@PathVariable id: String): ResponseNumberDto {
        val z = repo.findByIdOrNull(id)

        return z?.let { ResponseNumberDto.fromEntity(it) } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This user does not exist")
    }

}