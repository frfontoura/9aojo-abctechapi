package br.com.fiap.abctechapi.application.dto

import br.com.fiap.abctechapi.model.User

data class SignUpResponseDTO(
    val username: String,
    val email: String
) {

    constructor(user: User) : this(
        username = user.username,
        email = user.email
    )

}
