package br.com.fiap.abctechapi.service

import br.com.fiap.abctechapi.application.dto.OrderDTO
import br.com.fiap.abctechapi.application.dto.OrderLocationDTO
import br.com.fiap.abctechapi.enums.OrderLocationType
import br.com.fiap.abctechapi.enums.OrderStatus
import br.com.fiap.abctechapi.exceptions.BusinessException
import br.com.fiap.abctechapi.model.Order
import br.com.fiap.abctechapi.model.OrderLocation
import br.com.fiap.abctechapi.model.User
import br.com.fiap.abctechapi.repository.AssistanceRepository
import br.com.fiap.abctechapi.repository.OrderRepository
import mu.KLogger
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val assistanceRepository: AssistanceRepository,

    @Value("\${config.page.size}")
    private var pageSize: Int = 0
) {

    private val log: KLogger = KotlinLogging.logger { }

    fun createOrder(operator: User, orderDTO: OrderDTO): Order {
        log.info { "m=createOrder, i=initiated, userCode=${operator.userCode}, order=${orderDTO}" }

        val assists = orderDTO.assistsCodes.map { assistanceCode ->
            assistanceRepository.findByAssistanceCode(assistanceCode)
                ?: throw BusinessException(
                    message = "Assist\u00EAncia n\u00E3o encontrada",
                    description = "N\u00E3o foi encontrada uma assist\u00EAncia com o c\u00F3digo=$assistanceCode"
                ).also {
                    log.warn { "m=createOrder, e=assistance_not_found, userCode=${operator.userCode}, assistanceCode=$assistanceCode" }
                }
        }

        log.info { "m=createOrder, i=all_assists_found, userCode=${operator.userCode}" }

        val order = Order(
            operator = operator,
            assists = assists
        )

        val startOrderLocation = OrderLocation(
            order = order,
            date = orderDTO.startOrderLocation.dateTime,
            latitude = orderDTO.startOrderLocation.latitude,
            longitude = orderDTO.startOrderLocation.longitude,
            type = OrderLocationType.START
        )

        order.addOrderLocation(startOrderLocation)

        return orderRepository.save(order).also {
            log.info { "m=createOrder, i=finished, userCode=${operator.userCode}, orderCode=${order.orderCode}" }
        }
    }

    fun finalizeOrder(orderCode: UUID, orderLocationDTO: OrderLocationDTO): Order {
        log.info { "m=finalizeOrder, i=initiated, orderCode=$orderCode, orderLocationDTO=${orderLocationDTO}" }

        val order: Order = orderRepository.findByOrderCode(orderCode) ?: throw BusinessException(
            message = "Ordem n\u00E3o encontrada",
            description = "N\u00E3o foi poss\u00EDvel encontrar a ordem de c\u00F3digo=$orderCode"
        ).also {
            log.error { "m=finalizeOrder, e=order_not_found, orderCode=$orderCode" }
        }

        order.validateFinalizeOrder()

        val finishOrderLocation = OrderLocation(
            order = order,
            date = orderLocationDTO.dateTime,
            latitude = orderLocationDTO.latitude,
            longitude = orderLocationDTO.longitude,
            type = OrderLocationType.FINISH
        )

        order.addOrderLocation(finishOrderLocation)
        order.status = OrderStatus.FINISHED

        return orderRepository.save(order).also {
            log.info {"m=finalizeOrder, i=finished, orderCode=$orderCode, orderLocation=${finishOrderLocation}" }
        }
    }

    fun findByOperatorAndStatusIn(operatorId: Long, orderStatusList: List<OrderStatus>, page: Int): Page<Order> {
        log.info { "m=findByOperatorAndStatusIn, i=initiated, operatorId=$operatorId, orderStatusList=$orderStatusList, page=$page" }
        return orderRepository.findByOperatorIdAndStatusIn(operatorId, orderStatusList, PageRequest.of(page, pageSize, Sort.by("id")))
    }

}