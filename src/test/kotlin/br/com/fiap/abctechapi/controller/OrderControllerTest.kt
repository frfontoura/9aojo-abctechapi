package br.com.fiap.abctechapi.controller

import br.com.fiap.abctechapi.application.OrderApplication
import br.com.fiap.abctechapi.application.dto.OrderDTO
import br.com.fiap.abctechapi.application.dto.OrderLocationDTO
import br.com.fiap.abctechapi.enums.OrderStatus
import br.com.fiap.abctechapi.fixtures.OrderFixture
import br.com.fiap.abctechapi.fixtures.UserFixture
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import java.math.BigDecimal
import java.util.UUID

@MockKExtension.ConfirmVerification
@MockKExtension.CheckUnnecessaryStub
@ExtendWith(MockKExtension::class)
internal class OrderControllerTest {

    @MockK
    private lateinit var orderApplication: OrderApplication

    @InjectMockKs
    private lateinit var orderController: OrderController

    @Test
    fun createOrderTest() {
        val user = UserFixture.random()

        val requestOrderDTO = OrderDTO(
            assistsCodes = listOf(UUID.randomUUID()),
            startOrderLocation = OrderLocationDTO(
                latitude = BigDecimal.TEN,
                longitude = BigDecimal.ONE
            )
        )

        every { orderApplication.createOrder(user, requestOrderDTO) } returns OrderDTO(OrderFixture.random())

        val orderDTO = orderController.createOrder(user, requestOrderDTO)

        assertNotNull(orderDTO)

        verify(exactly = 1) { orderApplication.createOrder(user, requestOrderDTO) }
    }

    @Test
    fun finalizeOrderTest() {
        val order = OrderFixture.random()
        val orderCode = order.orderCode

        val orderLocationDTO = OrderLocationDTO(
            latitude = BigDecimal.ONE,
            longitude = BigDecimal.TEN
        )

        every { orderApplication.finalizeOrder(orderCode, orderLocationDTO) } returns OrderDTO(order)

        val orderDTO = orderController.finalizeOrder(orderCode, orderLocationDTO)

        assertNotNull(orderDTO)

        verify(exactly = 1) { orderApplication.finalizeOrder(orderCode, orderLocationDTO) }
    }

    @Test
    fun findByOperatorAndStatusInTest() {
        val user = UserFixture.random()
        val orderDTO = OrderDTO(OrderFixture.random())
        val pageMock = PageImpl(listOf(orderDTO))
        val orderStatusList = OrderStatus.values().toList()

        every { orderApplication.findByOperatorIdAndStatusIn(user.id!!, orderStatusList, 0) } returns pageMock

        val page = orderController.findByOperatorAndStatusIn(user, orderStatusList, 0)

        Assertions.assertFalse(page.isEmpty)

        verify(exactly = 1) { orderApplication.findByOperatorIdAndStatusIn(user.id!!, orderStatusList, 0) }
    }

}