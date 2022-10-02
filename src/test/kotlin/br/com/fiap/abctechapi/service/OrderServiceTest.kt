package br.com.fiap.abctechapi.service

import br.com.fiap.abctechapi.application.dto.OrderDTO
import br.com.fiap.abctechapi.application.dto.OrderLocationDTO
import br.com.fiap.abctechapi.enums.OrderLocationType
import br.com.fiap.abctechapi.enums.OrderStatus
import br.com.fiap.abctechapi.exceptions.BusinessException
import br.com.fiap.abctechapi.fixtures.AssistanceFixture
import br.com.fiap.abctechapi.fixtures.OrderFixture
import br.com.fiap.abctechapi.fixtures.UserFixture
import br.com.fiap.abctechapi.model.Order
import br.com.fiap.abctechapi.repository.AssistanceRepository
import br.com.fiap.abctechapi.repository.OrderRepository
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.math.BigDecimal
import java.util.UUID

@MockKExtension.CheckUnnecessaryStub
@ExtendWith(MockKExtension::class)
internal class OrderServiceTest {

    private val orderRepository = mockk<OrderRepository>()
    private val assistanceRepository = mockk<AssistanceRepository>()
    private val pageSize = 10

    private val orderService = OrderService(
        orderRepository = orderRepository,
        assistanceRepository = assistanceRepository,
        pageSize = pageSize
    )

    @Test
    fun createOrderTest() {
        val orderSlot = CapturingSlot<Order>()
        val assistance = AssistanceFixture.random()
        val user = UserFixture.random()

        val orderDTO = OrderDTO(
            assistsCodes = listOf(assistance.assistanceCode),
            startOrderLocation = OrderLocationDTO(
                latitude = BigDecimal.TEN,
                longitude = BigDecimal.ONE
            )
        )

        every { assistanceRepository.findByAssistanceCode(assistance.assistanceCode) } returns assistance
        every { orderRepository.save(capture(orderSlot)) } answers { orderSlot.captured }

        val order = orderService.createOrder(user, orderDTO)

        assertAll(
            { assertNotNull(order.orderCode) },
            { assertEquals(user, order.operator) },
            { assertTrue(order.assists.contains(assistance)) },
            { assertNotNull(order.getOrderLocationByType(OrderLocationType.START)) },
            { assertNotNull(order.getOrderLocationByType(OrderLocationType.START)?.orderLocationCode) },
            { assertEquals(BigDecimal.TEN, order.getOrderLocationByType(OrderLocationType.START)?.latitude) },
            { assertEquals(BigDecimal.ONE, order.getOrderLocationByType(OrderLocationType.START)?.longitude) },
            { assertEquals(order, order.getOrderLocationByType(OrderLocationType.START)?.order) }
        )

        verify(exactly = 1) { assistanceRepository.findByAssistanceCode(assistance.assistanceCode) }
        verify(exactly = 1) { orderRepository.save(any()) }

    }

    @Test
    fun createOrderAssistanceNotFoundTest() {
        val user = UserFixture.random()
        val assistanceCode = UUID.randomUUID()

        val orderDTO = OrderDTO(
            assistsCodes = listOf(assistanceCode),
            startOrderLocation = OrderLocationDTO(
                latitude = BigDecimal.TEN,
                longitude = BigDecimal.ONE
            )
        )

        every { assistanceRepository.findByAssistanceCode(assistanceCode) } returns null

        assertThrows<BusinessException> { orderService.createOrder(user, orderDTO) }

        verify(exactly = 1) { assistanceRepository.findByAssistanceCode(assistanceCode) }
    }

    @Test
    fun finalizeOrderTest() {
        val order = OrderFixture.random()

        val orderLocationDTO = OrderLocationDTO(
            latitude = BigDecimal.TEN,
            longitude = BigDecimal.ONE
        )

        every { orderRepository.findByOrderCode(order.orderCode) } returns order
        every { orderRepository.save(order) } returns order

        orderService.finalizeOrder(order.orderCode, orderLocationDTO)

        assertAll(
            { assertEquals(OrderStatus.FINISHED, order.status) },
            {
                val finishOrderLocation = order.getOrderLocationByType(OrderLocationType.FINISH)
                assertNotNull(finishOrderLocation)
                assertNotNull(finishOrderLocation?.orderLocationCode)
                assertEquals(order, finishOrderLocation?.order)
                assertEquals(BigDecimal.TEN, finishOrderLocation?.latitude)
                assertEquals(BigDecimal.ONE, finishOrderLocation?.longitude)
            }
        )

        verify(exactly = 1) { orderRepository.findByOrderCode(order.orderCode) }
        verify(exactly = 1) { orderRepository.save(order) }
    }

    @Test
    fun finalizeOrderOrderNotFoundTest() {
        val orderCode = UUID.randomUUID()

        val orderLocationDTO = OrderLocationDTO(
            latitude = BigDecimal.TEN,
            longitude = BigDecimal.ONE
        )

        every { orderRepository.findByOrderCode(orderCode) } returns null

        assertThrows<BusinessException> { orderService.finalizeOrder(orderCode, orderLocationDTO) }

        verify(exactly = 1) { orderRepository.findByOrderCode(orderCode) }
    }

    @Test
    fun findByOperatorAndStatusInTest() {
        val order = OrderFixture.random()
        val pageMock = PageImpl(listOf(order))
        val pageRequest = PageRequest.of(0, pageSize, Sort.by("id"))
        val orderStatusList = OrderStatus.values().toList()

        every { orderRepository.findByOperatorIdAndStatusIn(1L, orderStatusList, pageRequest) } returns pageMock

        val page = orderService.findByOperatorAndStatusIn(1L, orderStatusList, 0)

        assertFalse(page.isEmpty)
        assertTrue(page.contains(order))

        verify(exactly = 1) { orderRepository.findByOperatorIdAndStatusIn(1L, orderStatusList, pageRequest) }
    }

}