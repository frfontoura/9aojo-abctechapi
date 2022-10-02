package br.com.fiap.abctechapi.application.dto

import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class SignUpRequestDTO(

    @field:Size(min = 5, max = 50)
    val username: String,

    @field:Email
    val email: String,

    @field:Size(min = 8, max = 50)
    val password: String
)
