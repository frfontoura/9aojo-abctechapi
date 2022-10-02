package br.com.fiap.abctechapi.controller.handler

import java.time.LocalDateTime

data class ErrorMessageResponse(
    val statusCode: Int,
    val message: String,
    val description: String? = null,
    val time: LocalDateTime = LocalDateTime.now()
)
