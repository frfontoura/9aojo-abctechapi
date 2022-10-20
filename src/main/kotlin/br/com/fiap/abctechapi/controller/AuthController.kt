package br.com.fiap.abctechapi.controller

import br.com.fiap.abctechapi.application.AuthApplication
import br.com.fiap.abctechapi.application.dto.JwtResponseDTO
import br.com.fiap.abctechapi.application.dto.SignInRequestDTO
import br.com.fiap.abctechapi.application.dto.SignUpRequestDTO
import br.com.fiap.abctechapi.application.dto.SignUpResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KLogger
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Tag(name = "Auth Controller", description = "Endpoints relacionados ao cadastro e login de usu\u00E1rios")
@SecurityRequirements
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authApplication: AuthApplication
) {

    private val log: KLogger = KotlinLogging.logger { }

    @Operation(summary = "Realiza a autentica\u00E7\u00E3o do usu\u00E1rio, retorna um JWT")
    @PostMapping("/signin")
    fun signIn(@Valid @RequestBody signInRequestDTO: SignInRequestDTO): JwtResponseDTO {
        log.info { "m=signIn, i=initiated, username=${signInRequestDTO.username}" }
        return authApplication.signIn(signInRequestDTO)
    }

    @Operation(summary = "Realiza o cadastro de um novo usu\u00E1rio")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody signUpRequestDTO: SignUpRequestDTO): SignUpResponseDTO {
        log.info { "m=signUp, i=initiated, username=${signUpRequestDTO.username}" }
        return authApplication.signUp(signUpRequestDTO)
    }

}