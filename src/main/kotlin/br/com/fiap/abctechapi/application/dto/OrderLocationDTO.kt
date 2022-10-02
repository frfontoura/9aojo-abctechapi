package br.com.fiap.abctechapi.application.dto

import br.com.fiap.abctechapi.model.OrderLocation
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.validation.constraints.PastOrPresent

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrderLocationDTO(
    val orderLocationCode: UUID? = null,
    val latitude: BigDecimal,
    val longitude: BigDecimal,

    @field: PastOrPresent
    val dateTime: LocalDateTime = LocalDateTime.now()
) {

    constructor(orderLocation: OrderLocation) : this(
        orderLocationCode = orderLocation.orderLocationCode,
        latitude = orderLocation.latitude,
        longitude = orderLocation.longitude,
        dateTime = orderLocation.date
    )

}
