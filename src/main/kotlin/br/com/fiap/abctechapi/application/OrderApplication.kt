package br.com.fiap.abctechapi.application

import br.com.fiap.abctechapi.application.dto.OrderDTO
import br.com.fiap.abctechapi.application.dto.OrderLocationDTO
import br.com.fiap.abctechapi.enums.OrderStatus
import br.com.fiap.abctechapi.model.User
import br.com.fiap.abctechapi.service.OrderService
import mu.KLogger
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class OrderApplication(
    private val orderService: OrderService
) {

    private val log: KLogger = KotlinLogging.logger { }

    fun createOrder(user: User, orderDTO: OrderDTO): OrderDTO {
        log.info {"m=createOrder, i=initiated, userCode=${user.userCode}, orderDTO=$orderDTO" }

        val order = orderService.createOrder(user, orderDTO)

        log.info { "m=createOrder, i=finished, userCode=${user.userCode}, order=$order" }

        return OrderDTO(order)
    }

    fun finalizeOrder(orderCode: UUID, orderLocationDTO: OrderLocationDTO): OrderDTO {
        log.info { "m=finalizeOrder, i=initiated, orderCode=$orderCode, orderLocation=$orderLocationDTO" }

        val order = orderService.finalizeOrder(orderCode, orderLocationDTO)

        log.info {"m=finalizeOrder, i=finished, orderCode=$orderCode, orderLocation=$orderLocationDTO" }

        return OrderDTO(order)
    }

    fun findByOperatorIdAndStatusIn(operatorId: Long, orderStatusList: List<OrderStatus>, page: Int = 0): Page<OrderDTO> {
        log.info { "m=findByOperatorIdAndStatusIn, i=initiated, operatorId=$operatorId, orderStatusList=$orderStatusList, page=$page" }

        val ordersPage = orderService.findByOperatorAndStatusIn(operatorId, orderStatusList, page)

        log.info { "m=findByOperatorIdAndStatusIn, i=finished, operatorId=$operatorId, orderStatusList=$orderStatusList, page=$page, " +
            "totalElements=${ordersPage.totalElements}, totalPages=${ordersPage.totalPages}" }

        return ordersPage.map { order -> OrderDTO(order) }
    }

}