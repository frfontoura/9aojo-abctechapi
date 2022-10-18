package br.com.fiap.abctechapi.application.dto

import br.com.fiap.abctechapi.enums.OrderLocationType
import br.com.fiap.abctechapi.model.Order
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrderDetailDTO(
    val orderCode: UUID? = null,
    val assists: List<AssistanceDTO>,
    val startOrderLocation: OrderLocationDTO,
    val finishOrderLocation: OrderLocationDTO? = null
) {

    constructor(order: Order) : this(
        orderCode = order.orderCode,
        startOrderLocation = OrderLocationDTO(order.getOrderLocationByType(OrderLocationType.START)!!),
        finishOrderLocation = order.getOrderLocationByType(OrderLocationType.FINISH)?.run { OrderLocationDTO(this) },
        assists = order.assists.map {
            AssistanceDTO(
                assistanceCode = it.assistanceCode,
                name = it.name,
                description = it.description
            )
        }
    )

}
