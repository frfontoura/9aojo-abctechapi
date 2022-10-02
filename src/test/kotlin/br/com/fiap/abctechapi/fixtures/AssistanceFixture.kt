package br.com.fiap.abctechapi.fixtures

import br.com.fiap.abctechapi.model.Assistance
import java.util.UUID

object AssistanceFixture {

    fun random(
        id: Long = 1L,
        name: String = "Assistance 1",
        description: String = "Description 1",
        assistanceCode: UUID = UUID.randomUUID()
    ) = Assistance(
        id = id,
        name = name,
        description = description,
        assistanceCode = assistanceCode
    )

}