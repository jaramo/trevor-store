package io.trevor.store.number.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import io.trevor.store.number.repository.entity.NumberEntity

@JsonAutoDetect
data class ResponseNumberDto(val id: String, val value: Int)
{
    companion object Factory {
        fun fromEntity(entity: NumberEntity): ResponseNumberDto =
            ResponseNumberDto(entity.id.orEmpty(), entity.value)
    }
}
