package br.com.fiap.abctechapi.application.dto

data class JwtResponseDTO(
    val token: String,
    val type: String = "Bearer"
)
