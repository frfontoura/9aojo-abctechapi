package br.com.fiap.abctechapi.fixtures

import br.com.fiap.abctechapi.enums.OrderStatus
import br.com.fiap.abctechapi.model.Assistance
import br.com.fiap.abctechapi.model.Order
import br.com.fiap.abctechapi.model.User
import java.util.UUID

object OrderFixture {

    fun random(
        id: Long = 1L,
        orderCode: UUID = UUID.randomUUID(),
        status: OrderStatus = OrderStatus.STARTED,
        user: User = UserFixture.random(),
        assists: List<Assistance> = listOf(AssistanceFixture.random())
    ) = Order(
        id = id,
        orderCode = orderCode,
        status = status,
        operator = user,
        assists = assists,
    ).also {
        it.addOrderLocation(OrderLocationFixture.random(order = it))
    }

}