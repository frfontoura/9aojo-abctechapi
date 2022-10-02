package br.com.fiap.abctechapi.service

import br.com.fiap.abctechapi.application.dto.JwtResponseDTO
import br.com.fiap.abctechapi.application.dto.SignInRequestDTO
import br.com.fiap.abctechapi.application.dto.SignUpRequestDTO
import br.com.fiap.abctechapi.config.security.JwtUtil
import br.com.fiap.abctechapi.enums.RoleType
import br.com.fiap.abctechapi.exceptions.BusinessException
import br.com.fiap.abctechapi.model.User
import br.com.fiap.abctechapi.repository.RoleRepository
import br.com.fiap.abctechapi.repository.UserRepository
import mu.KLogger
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) {

    private val log: KLogger = KotlinLogging.logger { }

    fun signIn(signInRequestDTO: SignInRequestDTO): JwtResponseDTO {
        log.info { "m=signUp, i=initiated, username=${signInRequestDTO.username}" }

        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(signInRequestDTO.username, signInRequestDTO.password))
        SecurityContextHolder.getContext().authentication = authentication

        log.info { "m=signUp, i=authenticated, username=${signInRequestDTO.username}" }

        return JwtResponseDTO(
            token = jwtUtil.generateJwtToken(authentication)
        )
    }

    fun signUp(signUpRequestDTO: SignUpRequestDTO): User {
        log.info { "m=signUp, i=initiated, username=${signUpRequestDTO.username}" }

        validateNewUser(signUpRequestDTO)

        val hashedPassword = passwordEncoder.encode(signUpRequestDTO.password)

        val userRole = roleRepository.findByName(RoleType.ROLE_USER) ?: throw BusinessException("Nenhuma role encontrada").also {
            log.error { "m=signUp, e=no_roles_found, username=${signUpRequestDTO.username}" }
        }

        return userRepository.save(User(
            username = signUpRequestDTO.username,
            email = signUpRequestDTO.email,
            password = hashedPassword,
            roles = setOf(userRole)
        )).also {
            log.info {"m=signUp, i=finished, username=${signUpRequestDTO.username}" }
        }
    }

    private fun validateNewUser(signUpRequestDTO: SignUpRequestDTO) {
        log.info { "m=validateNewUser, e=initiated, username=${signUpRequestDTO.username}" }

        if (userRepository.existsByUsername(signUpRequestDTO.username)) {
            log.warn { "m=validateNewUser, e=username_already_registered, username=${signUpRequestDTO.username}" }
            throw BusinessException("Usu\u00E1rio j\u00E1 cadastrado")
        }

        if (userRepository.existsByEmail(signUpRequestDTO.email)) {
            log.warn { "m=validateNewUser, e=email_already_registered, username=${signUpRequestDTO.username}, email=${signUpRequestDTO.email}" }
            throw BusinessException("Usu\u00E1rio j\u00E1 cadastrado")
        }

        log.info { "m=validateNewUser, e=finished, username=${signUpRequestDTO.username}" }
    }
}