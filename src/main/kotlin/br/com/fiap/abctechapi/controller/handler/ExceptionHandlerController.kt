package br.com.fiap.abctechapi.controller.handler

import br.com.fiap.abctechapi.exceptions.BusinessException
import mu.KLogger
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandlerController {

    private val log: KLogger = KotlinLogging.logger { }

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): ResponseEntity<ErrorMessageResponse> {
        log.error(e) { "m=exceptionHandler, e=${e.javaClass.name}, message=${e.localizedMessage}" }
        val message = "N\u00E3o foi poss\u00EDvel processar a sua solicita\u00E7\u00E3o"
        val description = "N\u00E3o foi poss\u00EDvel processar a sua solicita\u00E7\u00E3o, tente novamente mais tarde."
        return getErrorMessageResponseResponseEntity(message, description, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(BusinessException::class)
    fun businessExceptionHandler(e: BusinessException): ResponseEntity<ErrorMessageResponse> {
        log.warn { "m=businessExceptionHandler, e=${e.javaClass.name}, message=${e.localizedMessage}, description=${e.description}" }
        return getErrorMessageResponseResponseEntity(
            message = e.localizedMessage,
            description = e.description,
            statusCode = e.httpStatus
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ErrorMessageResponse> {
        val errors = ex.bindingResult.allErrors.map { "${(it as FieldError).field}: ${it.defaultMessage}" }
        return getErrorMessageResponseResponseEntity(
            message = "Dados incorretos",
            description = errors.joinToString("\n"),
            statusCode = HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<ErrorMessageResponse> {
        log.warn { "m=handleBadCredentialsException, i=invalid_username_or_password" }
        return getErrorMessageResponseResponseEntity(message = "Usu\u00E1rio ou senha incorretos", statusCode = HttpStatus.BAD_REQUEST)
    }

    private fun getErrorMessageResponseResponseEntity(
        message: String,
        description: String? = null,
        statusCode: HttpStatus
    ) = ResponseEntity(
        ErrorMessageResponse(
            message = message,
            description = description,
            statusCode = statusCode.value(),
        ), statusCode)

}