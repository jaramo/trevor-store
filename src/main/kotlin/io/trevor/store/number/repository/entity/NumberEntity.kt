package io.trevor.store.number.repository.entity

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class NumberEntity(
    @Id var id: String? = null,
    var value: Int
) {
    companion object Factory {
        fun create(value: Int): NumberEntity =
            NumberEntity(UUID.randomUUID().toString(), value)
    }
}