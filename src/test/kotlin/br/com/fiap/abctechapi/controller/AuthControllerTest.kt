package br.com.fiap.abctechapi.controller

import br.com.fiap.abctechapi.application.AuthApplication
import br.com.fiap.abctechapi.application.dto.JwtResponseDTO
import br.com.fiap.abctechapi.application.dto.SignInRequestDTO
import br.com.fiap.abctechapi.application.dto.SignUpRequestDTO
import br.com.fiap.abctechapi.application.dto.SignUpResponseDTO
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@MockKExtension.ConfirmVerification
@MockKExtension.CheckUnnecessaryStub
@ExtendWith(MockKExtension::class)
internal class AuthControllerTest {

    @MockK
    private lateinit var authApplication: AuthApplication

    @InjectMockKs
    private lateinit var authController: AuthController

    @Test
    fun signInTest() {
        val signInRequestDTO = SignInRequestDTO(
            username = "user1",
            password = "password1"
        )

        val token = UUID.randomUUID().toString()

        every { authApplication.signIn(signInRequestDTO) } returns JwtResponseDTO(token)

        val jwtResponseDTO = authController.signIn(signInRequestDTO)

        assertEquals(token, jwtResponseDTO.token)

        verify(exactly = 1) { authApplication.signIn(signInRequestDTO) }
    }

    @Test
    fun signUpTest() {
        val signUpRequestDTO = SignUpRequestDTO(
            username = "user2",
            email = "email@email.com",
            password = "asdf1234"
        )

        every { authApplication.signUp(signUpRequestDTO) } returns SignUpResponseDTO(
            username = signUpRequestDTO.username,
            email = signUpRequestDTO.email
        )

        val signUpResponseDTO = authController.signUp(signUpRequestDTO)

        assertEquals(signUpRequestDTO.username, signUpResponseDTO.username)
        assertEquals(signUpRequestDTO.email, signUpResponseDTO.email)

        verify(exactly = 1) { authApplication.signUp(signUpRequestDTO) }
    }

}