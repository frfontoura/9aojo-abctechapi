package br.com.fiap.abctechapi.service

import br.com.fiap.abctechapi.model.Assistance
import br.com.fiap.abctechapi.repository.AssistanceRepository
import mu.KLogger
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class AssistanceService(
    private val assistanceRepository: AssistanceRepository,

    @Value("\${config.page.size}")
    private var pageSize: Int = 0
) {

    private val log: KLogger = KotlinLogging.logger { }

    fun getAssists(page: Int): Page<Assistance> {
        log.info { "m=getAssistanceList, i=initiated, pageSize=$pageSize, page=$page" }
        val assists: Page<Assistance> = assistanceRepository.findAll(PageRequest.of(page, pageSize, Sort.by("name")))
        log.info { "m=getAssistanceList, i=finished, pageSize=$pageSize, page=$page, totalElements=${assists.totalElements}, totalPages=${assists.totalPages}" }
        return assists
    }

}