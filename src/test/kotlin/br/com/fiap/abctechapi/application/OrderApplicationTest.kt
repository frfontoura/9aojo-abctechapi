package br.com.fiap.abctechapi.application

import br.com.fiap.abctechapi.application.dto.OrderDTO
import br.com.fiap.abctechapi.application.dto.OrderLocationDTO
import br.com.fiap.abctechapi.enums.OrderStatus
import br.com.fiap.abctechapi.fixtures.OrderFixture
import br.com.fiap.abctechapi.fixtures.UserFixture
import br.com.fiap.abctechapi.service.OrderService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import java.math.BigDecimal
import java.util.UUID

@MockKExtension.ConfirmVerification
@MockKExtension.CheckUnnecessaryStub
@ExtendWith(MockKExtension::class)
internal class OrderApplicationTest {

    private val orderService = mockk<OrderService>()

    private val orderApplication = OrderApplication(
        orderService = orderService
    )

    @Test
    fun createOrderTest() {
        val user = UserFixture.random()
        val order = OrderFixture.random()

        val requestOrderDTO = OrderDTO(
            assistsCodes = listOf(UUID.randomUUID()),
            startOrderLocation = OrderLocationDTO(
                latitude = BigDecimal.TEN,
                longitude = BigDecimal.ONE
            )
        )

        every { orderService.createOrder(user, requestOrderDTO) } returns order

        val orderDTO = orderApplication.createOrder(user, requestOrderDTO)

        assertNotNull(orderDTO)

        verify(exactly = 1) { orderService.createOrder(user, requestOrderDTO) }
    }

    @Test
    fun finalizeOrderTest() {
        val order = OrderFixture.random()
        val orderCode = order.orderCode

        val orderLocationDTO = OrderLocationDTO(
            latitude = BigDecimal.ONE,
            longitude = BigDecimal.TEN
        )

        every { orderService.finalizeOrder(orderCode, orderLocationDTO) } returns order

        val orderDTO = orderApplication.finalizeOrder(orderCode, orderLocationDTO)

        assertNotNull(orderDTO)

        verify(exactly = 1) { orderService.finalizeOrder(orderCode, orderLocationDTO) }
    }

    @Test
    fun findByOperatorIdAndStatusIn() {
        val order = OrderFixture.random()
        val pageMock = PageImpl(listOf(order))
        val orderStatusList = OrderStatus.values().toList()

        every { orderService.findByOperatorAndStatusIn(1L, orderStatusList, 0) } returns pageMock

        val page = orderApplication.findByOperatorIdAndStatusIn(1L, orderStatusList, 0)

        assertFalse(page.isEmpty)

        verify(exactly = 1) { orderService.findByOperatorAndStatusIn(1L, orderStatusList, 0) }
    }

}