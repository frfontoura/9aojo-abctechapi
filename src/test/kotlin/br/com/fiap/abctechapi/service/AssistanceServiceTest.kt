package br.com.fiap.abctechapi.service

import br.com.fiap.abctechapi.fixtures.AssistanceFixture
import br.com.fiap.abctechapi.repository.AssistanceRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@MockKExtension.ConfirmVerification
@MockKExtension.CheckUnnecessaryStub
@ExtendWith(MockKExtension::class)
class AssistanceServiceTest {

    private val assistanceRepository = mockk<AssistanceRepository>()
    private val assistanceService = AssistanceService(
        assistanceRepository = assistanceRepository,
        pageSize = 10
    )

    @Test
    fun getAssistsSuccessTest() {
        val assists = listOf(
            AssistanceFixture.random(),
            AssistanceFixture.random(),
            AssistanceFixture.random(),
        )

        val pageRequest = PageRequest.of(1, 10, Sort.by("name"))
        val pageMock = PageImpl(assists)

        every { assistanceRepository.findAll(pageRequest) } returns pageMock

        val page = assistanceService.getAssists(1);

        assertEquals(3, page.totalElements)
        assertTrue(page.toList().containsAll(assists))

        verify(exactly = 1) { assistanceRepository.findAll(pageRequest) }
    }

    @Test
    fun getAssistsEmptyTest() {
        val pageRequest = PageRequest.of(1, 10, Sort.by("name"))

        every { assistanceRepository.findAll(pageRequest) } returns Page.empty()

        val page = assistanceService.getAssists(1)

        assertTrue(page.isEmpty)

        verify(exactly = 1) { assistanceRepository.findAll(pageRequest) }
    }

}