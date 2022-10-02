package br.com.fiap.abctechapi.application

import br.com.fiap.abctechapi.fixtures.AssistanceFixture
import br.com.fiap.abctechapi.model.Assistance
import br.com.fiap.abctechapi.service.AssistanceService
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
class AssistanceApplicationTest {

    @MockK
    private lateinit var assistanceService: AssistanceService

    @InjectMockKs
    private lateinit var assistanceApplication: AssistanceApplication

    @Test
    fun getAssistanceListSuccessTest() {
        val assistance = AssistanceFixture.random()
        val assists = listOf(assistance)
        val pageMock = PageImpl(assists)

        every { assistanceService.getAssists(1) } returns pageMock

        val page = assistanceApplication.getAssists(1)

        assertAll(
            { assertEquals(1L, page.totalElements) },
            { assertEquals(assistance.name, page.first().name) },
            { assertEquals(assistance.assistanceCode, page.first().assistanceCode) },
            { assertEquals(assistance.description, page.first().description) }
        )

        verify(exactly = 1) { assistanceService.getAssists(1) }
    }

    @Test
    fun getAssistanceEmptyListTest() {
        val pageMock = PageImpl<Assistance>(emptyList())

        every { assistanceService.getAssists(1) } returns pageMock

        val page = assistanceApplication.getAssists(1)

        assertEquals(0, page.totalElements)
        verify(exactly = 1) { assistanceService.getAssists(1) }
    }

}