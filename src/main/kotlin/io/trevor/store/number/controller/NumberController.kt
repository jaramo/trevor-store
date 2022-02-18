package io.trevor.store.number.controller

import io.trevor.store.number.dto.CreateNumberDto
import io.trevor.store.number.dto.ResponseNumberDto
import io.trevor.store.number.exception.NumberNotFoundException
import io.trevor.store.number.exception.UnableToStoreNumberException
import io.trevor.store.number.service.NumberService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/numbers")
class NumberController(private val numberService: NumberService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(): List<ResponseNumberDto> {
        return numberService.getAll()
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateNumberDto): ResponseNumberDto =
        numberService.store(request.value) ?: throw UnableToStoreNumberException()

    @GetMapping(path = ["/{id}"])
    fun getNumber(@PathVariable id: String): ResponseNumberDto =
        numberService.get(id) ?: throw NumberNotFoundException()

}