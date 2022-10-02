package br.com.fiap.abctechapi.service

import br.com.fiap.abctechapi.application.dto.SignInRequestDTO
import br.com.fiap.abctechapi.application.dto.SignUpRequestDTO
import br.com.fiap.abctechapi.config.security.JwtUtil
import br.com.fiap.abctechapi.enums.RoleType
import br.com.fiap.abctechapi.exceptions.BusinessException
import br.com.fiap.abctechapi.model.Role
import br.com.fiap.abctechapi.model.User
import br.com.fiap.abctechapi.repository.RoleRepository
import br.com.fiap.abctechapi.repository.UserRepository
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

@MockKExtension.ConfirmVerification
@MockKExtension.CheckUnnecessaryStub
@ExtendWith(MockKExtension::class)
class AuthServiceTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var roleRepository: RoleRepository

    @MockK
    private lateinit var passwordEncoder: PasswordEncoder

    @MockK
    private lateinit var authenticationManager: AuthenticationManager

    @MockK
    private lateinit var jwtUtil: JwtUtil

    @InjectMockKs
    private lateinit var authService: AuthService

    @Test
    fun signInTest() {
        val signInRequestDTO = SignInRequestDTO("user", "password")
        val authentication = mockk<Authentication>()
        val token = UUID.randomUUID().toString()

        every { authenticationManager.authenticate(any()) } returns authentication
        every { jwtUtil.generateJwtToken(authentication) } returns token

        val jwtResponseDTO = authService.signIn(signInRequestDTO)

        assertNotNull(jwtResponseDTO)
        assertEquals(token, jwtResponseDTO.token)

        verify(exactly = 1) { authenticationManager.authenticate(any()) }
        verify(exactly = 1) { jwtUtil.generateJwtToken(authentication) }
    }

    @Test
    fun signUpTest() {
        val signUpRequestDTO = SignUpRequestDTO(
            username = "user",
            email = "user@mock.com",
            password = "asdf1234!"
        )

        val hashedPassword = UUID.randomUUID().toString()
        val role = Role(name = RoleType.ROLE_USER)
        val userSlot = CapturingSlot<User>()

        every { userRepository.existsByUsername(signUpRequestDTO.username) } returns false
        every { userRepository.existsByEmail(signUpRequestDTO.email) } returns false
        every { passwordEncoder.encode(signUpRequestDTO.password) } returns hashedPassword
        every { roleRepository.findByName(RoleType.ROLE_USER) } returns role
        every { userRepository.save(capture(userSlot)) } answers { userSlot.captured }

        val user = authService.signUp(signUpRequestDTO)

        assertAll(
            { assertEquals(signUpRequestDTO.username, user.username) },
            { assertEquals(signUpRequestDTO.email, user.email) },
            { assertEquals(hashedPassword, user.password) },
            { assertTrue(user.roles.contains(role)) }
        )

        verify(exactly = 1) { userRepository.existsByUsername(signUpRequestDTO.username) }
        verify(exactly = 1) { userRepository.existsByEmail(signUpRequestDTO.email) }
        verify(exactly = 1) { passwordEncoder.encode(signUpRequestDTO.password) }
        verify(exactly = 1) { roleRepository.findByName(RoleType.ROLE_USER) }
        verify(exactly = 1) { userRepository.save(capture(userSlot)) }
    }

    @Test
    fun signUpExistingUsernameTest() {
        val signUpRequestDTO = SignUpRequestDTO(
            username = "user",
            email = "user@mock.com",
            password = "asdf1234!"
        )

        every { userRepository.existsByUsername(signUpRequestDTO.username) } returns true

        assertThrows<BusinessException> { authService.signUp(signUpRequestDTO) }

        verify(exactly = 1) { userRepository.existsByUsername(signUpRequestDTO.username) }
    }

    @Test
    fun signUpExistingEmailTest() {
        val signUpRequestDTO = SignUpRequestDTO(
            username = "user",
            email = "user@mock.com",
            password = "asdf1234!"
        )

        every { userRepository.existsByUsername(signUpRequestDTO.username) } returns false
        every { userRepository.existsByEmail(signUpRequestDTO.email) } returns true

        assertThrows<BusinessException> { authService.signUp(signUpRequestDTO) }

        verify(exactly = 1) { userRepository.existsByUsername(signUpRequestDTO.username) }
        verify(exactly = 1) { userRepository.existsByEmail(signUpRequestDTO.email) }
    }

    @Test
    fun signUpNoRoleFoundTest() {
        val signUpRequestDTO = SignUpRequestDTO(
            username = "user",
            email = "user@mock.com",
            password = "asdf1234!"
        )

        val hashedPassword = UUID.randomUUID().toString()

        every { userRepository.existsByUsername(signUpRequestDTO.username) } returns false
        every { userRepository.existsByEmail(signUpRequestDTO.email) } returns false
        every { passwordEncoder.encode(signUpRequestDTO.password) } returns hashedPassword
        every { roleRepository.findByName(RoleType.ROLE_USER) } returns null

        assertThrows<BusinessException> { authService.signUp(signUpRequestDTO) }

        verify(exactly = 1) { userRepository.existsByUsername(signUpRequestDTO.username) }
        verify(exactly = 1) { userRepository.existsByEmail(signUpRequestDTO.email) }
        verify(exactly = 1) { passwordEncoder.encode(signUpRequestDTO.password) }
        verify(exactly = 1) { roleRepository.findByName(RoleType.ROLE_USER) }
    }

}