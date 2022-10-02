package br.com.fiap.abctechapi.controller

import br.com.fiap.abctechapi.application.AssistanceApplication
import br.com.fiap.abctechapi.application.dto.AssistanceDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KLogger
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Assists Controller", description = "Endpoints relacionados as assist\u00EAncias")
@RestController
@RequestMapping("/assists")
class AssistanceController(
    private val assistanceApplication: AssistanceApplication
) {

    private val log: KLogger = KotlinLogging.logger { }

    @Operation(summary = "Retorna a lista de assist\u00EAncias dispon\u00EDveis")
    @GetMapping
    fun getAssists(@RequestParam(required = false, defaultValue = "0") page: Int): Page<AssistanceDTO> {
        log.info { "m=getAssists, i=initiated, page=$page" }
        return assistanceApplication.getAssists(page)
    }

}