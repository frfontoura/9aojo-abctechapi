package br.com.fiap.abctechapi.application.dto

import javax.validation.constraints.Size

data class SignInRequestDTO(

    @field:Size(min = 5, max = 50)
    var username:  String,

    @field:Size(min = 8, max = 50)
    val password: String
)
