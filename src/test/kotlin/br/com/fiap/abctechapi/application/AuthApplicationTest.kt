package br.com.fiap.abctechapi.application

import br.com.fiap.abctechapi.application.dto.JwtResponseDTO
import br.com.fiap.abctechapi.application.dto.SignInRequestDTO
import br.com.fiap.abctechapi.application.dto.SignUpRequestDTO
import br.com.fiap.abctechapi.model.User
import br.com.fiap.abctechapi.service.AuthService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@MockKExtension.ConfirmVerification
@MockKExtension.CheckUnnecessaryStub
@ExtendWith(MockKExtension::class)
class AuthApplicationTest {

    @MockK
    private lateinit var authService: AuthService

    @InjectMockKs
    private lateinit var authApplication: AuthApplication

    @Test
    fun signInTest() {
        val signInRequestDTO = SignInRequestDTO("user", "password")
        val token = UUID.randomUUID().toString()

        every { authService.signIn(signInRequestDTO) } returns JwtResponseDTO(token)

        val jwtResponseDTO = authApplication.signIn(signInRequestDTO)

        assertNotNull(jwtResponseDTO)
        assertEquals(token, jwtResponseDTO.token)

        verify(exactly = 1) { authService.signIn(signInRequestDTO) }
    }

    @Test
    fun signUpTest() {
        val signUpRequestDTO = SignUpRequestDTO(
            username = "user",
            email = "user@mock.com",
            password = "1234567890"
        )

        val user = User(
            id = 1L,
            username = signUpRequestDTO.username,
            email = signUpRequestDTO.email,
            password = UUID.randomUUID().toString()
        )

        every { authService.signUp(signUpRequestDTO) } returns user

        val signUpResponseDTO = authApplication.signUp(signUpRequestDTO)

        assertAll(
            { assertEquals(signUpRequestDTO.username, signUpResponseDTO.username) },
            { assertEquals(signUpRequestDTO.email, signUpResponseDTO.email) }
        )

        verify(exactly = 1) { authService.signUp(signUpRequestDTO) }
    }

}