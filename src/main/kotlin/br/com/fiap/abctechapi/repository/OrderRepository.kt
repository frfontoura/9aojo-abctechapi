package br.com.fiap.abctechapi.repository

import br.com.fiap.abctechapi.enums.OrderStatus
import br.com.fiap.abctechapi.model.Order
import br.com.fiap.abctechapi.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderRepository: JpaRepository<Order, Long> {

    fun findByOrderCode(orderCode: UUID): Order?
    fun findByOperatorIdAndStatusIn(operatorId: Long, orderStatusList: List<OrderStatus>, pageRequest: PageRequest): Page<Order>
    fun findByOrderCodeAndOperator(orderCode: UUID, user: User): Order?

}