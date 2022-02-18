package io.trevor.store.number.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Number not found")
class UnableToStoreNumberException: RuntimeException()