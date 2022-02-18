package io.trevor.store.number.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateNumberDto(@JsonProperty(required = true) val value: Int)
