package io.trevor.store.number.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Number not found")
class NumberNotFoundException : RuntimeException()
