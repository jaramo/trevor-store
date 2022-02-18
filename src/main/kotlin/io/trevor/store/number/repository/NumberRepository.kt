package io.trevor.store.number.repository

import io.trevor.store.number.repository.entity.NumberEntity
import org.springframework.data.repository.CrudRepository

interface NumberRepository : CrudRepository<NumberEntity, String> {
}