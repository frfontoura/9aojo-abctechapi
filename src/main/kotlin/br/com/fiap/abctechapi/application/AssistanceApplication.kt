package br.com.fiap.abctechapi.application

import br.com.fiap.abctechapi.application.dto.AssistanceDTO
import br.com.fiap.abctechapi.service.AssistanceService
import mu.KLogger
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component

@Component
class AssistanceApplication(
    private val assistanceService: AssistanceService
) {

    private val log: KLogger = KotlinLogging.logger { }

    fun getAssists(page: Int): Page<AssistanceDTO> {
        log.info { "m=getAssists, i=initiated, page=$page" }
        return assistanceService.getAssists(page).map { AssistanceDTO(it) }
    }

}
