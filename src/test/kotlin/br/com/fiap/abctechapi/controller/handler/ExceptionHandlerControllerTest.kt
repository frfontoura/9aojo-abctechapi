package br.com.fiap.abctechapi.controller.handler

import br.com.fiap.abctechapi.exceptions.BusinessException
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus

@MockKExtension.ConfirmVerification
@MockKExtension.CheckUnnecessaryStub
@ExtendWith(MockKExtension::class)
internal class ExceptionHandlerControllerTest {

    @InjectMockKs
    private lateinit var exceptionHandlerController: ExceptionHandlerController

    @Test
    fun exceptionHandlerTest() {
        val exception = RuntimeException("Test")

        val responseEntity = exceptionHandlerController.exceptionHandler(exception)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.statusCode)
    }

    @Test
    fun businessExceptionHandlerTest() {
        val exception = BusinessException(message = "Message", description = "Description")

        val responseEntity = exceptionHandlerController.businessExceptionHandler(exception)

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.statusCode)
        assertEquals("Message", responseEntity.body?.message)
        assertEquals("Description", responseEntity.body?.description)
    }

}