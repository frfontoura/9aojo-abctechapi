package br.com.fiap.abctechapi.fixtures

import br.com.fiap.abctechapi.enums.OrderLocationType
import br.com.fiap.abctechapi.model.Order
import br.com.fiap.abctechapi.model.OrderLocation
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

object OrderLocationFixture {

    fun random(
        id: Long = 1L,
        orderLocationCode: UUID = UUID.randomUUID(),
        latitude: BigDecimal = BigDecimal.TEN,
        longitude: BigDecimal = BigDecimal.ONE,
        date: LocalDateTime = LocalDateTime.now(),
        type: OrderLocationType = OrderLocationType.START,
        order: Order
    ) = OrderLocation(
        id = id,
        orderLocationCode = orderLocationCode,
        latitude = latitude,
        longitude = longitude,
        date = date,
        type = type,
        order = order
    )

}