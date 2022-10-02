package br.com.fiap.abctechapi.exceptions

import org.springframework.http.HttpStatus

class BusinessException(
    message: String? = null,
    cause: Throwable? = null,
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    val description: String? = null
) : RuntimeException(message, cause)
