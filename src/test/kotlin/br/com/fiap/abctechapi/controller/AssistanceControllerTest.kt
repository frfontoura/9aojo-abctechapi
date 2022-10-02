package br.com.fiap.abctechapi.controller

import br.com.fiap.abctechapi.application.AssistanceApplication
import br.com.fiap.abctechapi.application.dto.AssistanceDTO
import br.com.fiap.abctechapi.fixtures.AssistanceFixture
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl

@MockKExtension.ConfirmVerification
@MockKExtension.CheckUnnecessaryStub
@ExtendWith(MockKExtension::class)
class AssistanceControllerTest {

    @MockK
    private lateinit var assistanceApplication: AssistanceApplication

    @InjectMockKs
    private lateinit var assistanceController: AssistanceController

    @Test
    fun getAssistanceListSuccessTest() {
        val assistance = AssistanceDTO(AssistanceFixture.random())
        val assists = listOf(assistance)
        val pageMock = PageImpl(assists)

        every { assistanceApplication.getAssists(1) } returns pageMock

        val page = assistanceController.getAssists(1)

        assertAll(
            { assertEquals(1L, page.totalElements) },
            { assertEquals(assistance.name, page.first().name) },
            { assertEquals(assistance.assistanceCode, page.first().assistanceCode) },
            { assertEquals(assistance.description, page.first().description) }
        )

        verify(exactly = 1) { assistanceApplication.getAssists(1) }
    }

}