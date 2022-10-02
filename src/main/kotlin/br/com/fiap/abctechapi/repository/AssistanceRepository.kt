package br.com.fiap.abctechapi.repository

import br.com.fiap.abctechapi.model.Assistance
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AssistanceRepository: JpaRepository<Assistance, Long> {

    fun findByAssistanceCode(assistanceCode: UUID): Assistance?

}