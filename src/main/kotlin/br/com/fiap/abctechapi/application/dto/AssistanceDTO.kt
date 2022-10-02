package br.com.fiap.abctechapi.application.dto

import br.com.fiap.abctechapi.model.Assistance
import java.util.UUID

data class AssistanceDTO(
    val assistanceCode: UUID,
    val name: String,
    val description: String
) {
    constructor(assistance: Assistance) : this(
        assistanceCode = assistance.assistanceCode,
        name = assistance.name,
        description = assistance.description
    )
}
