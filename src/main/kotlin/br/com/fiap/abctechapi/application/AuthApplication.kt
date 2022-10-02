package br.com.fiap.abctechapi.application

import br.com.fiap.abctechapi.application.dto.JwtResponseDTO
import br.com.fiap.abctechapi.application.dto.SignInRequestDTO
import br.com.fiap.abctechapi.application.dto.SignUpRequestDTO
import br.com.fiap.abctechapi.application.dto.SignUpResponseDTO
import br.com.fiap.abctechapi.service.AuthService
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class AuthApplication(
    private val authService: AuthService
) {

    private val log: KLogger = KotlinLogging.logger { }

    fun signIn(signInRequestDTO: SignInRequestDTO): JwtResponseDTO {
        log.info { "m=signIn, i=initiated, username=${signInRequestDTO.username}" }
        return authService.signIn(signInRequestDTO)
    }

    fun signUp(signUpRequestDTO: SignUpRequestDTO): SignUpResponseDTO {
        log.info { "m=signUp, i=initiated, username=${signUpRequestDTO.username}" }
        return SignUpResponseDTO(authService.signUp(signUpRequestDTO))
    }

}