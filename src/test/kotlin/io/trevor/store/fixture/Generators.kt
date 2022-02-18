package io.trevor.store.fixture

import java.util.UUID

object Generators {
    fun uuid(): String = UUID.randomUUID().toString()
    fun randomInt(): Int = (Int.MIN_VALUE..Int.MAX_VALUE).random()
}