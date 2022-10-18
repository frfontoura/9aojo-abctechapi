package br.com.fiap.abctechapi.controller

import br.com.fiap.abctechapi.application.OrderApplication
import br.com.fiap.abctechapi.application.dto.OrderDTO
import br.com.fiap.abctechapi.application.dto.OrderDetailDTO
import br.com.fiap.abctechapi.application.dto.OrderLocationDTO
import br.com.fiap.abctechapi.config.security.CurrentUser
import br.com.fiap.abctechapi.enums.OrderStatus
import br.com.fiap.abctechapi.model.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KLogger
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@Tag(name = "Orders Controller", description = "Endpoints relacionados as ordens")
@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderApplication: OrderApplication
) {

    private val log: KLogger = KotlinLogging.logger { }

    @Operation(summary = "Cria uma nova ordem de servi\u00E7o")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(
        @CurrentUser user: User,
        @RequestBody orderDTO: @Valid OrderDTO
    ): OrderDTO {
        log.info { "m=createOrder, i=initiated, userCode=${user.userCode}, orderDTO=${orderDTO}" }
        return orderApplication.createOrder(user, orderDTO)
    }

    @Operation(summary = "Finaliza uma ordem de servi\u00E7o")
    @PutMapping("/{orderCode}/finalize")
    fun finalizeOrder(
        @PathVariable orderCode: UUID,
        @RequestBody orderLocationDTO: @Valid OrderLocationDTO
    ): OrderDTO {
        log.info { "m=finalizeOrder, i=initiated, orderCode=$orderCode, orderLocation=${orderLocationDTO}" }
        return orderApplication.finalizeOrder(orderCode, orderLocationDTO)
    }

    @Operation(
        summary = "Busca as ordens de um operador",
        description = "Busca as ordens de um operador, por default busca as ordens ativas, sendo poss\u00EDvel buscar as finalizadas ou ambas."
    )
    @GetMapping
    fun findByOperatorAndStatusIn(
        @CurrentUser user: User,
        @RequestParam(defaultValue = "STARTED") orderStatusList: List<OrderStatus>,
        @RequestParam(required = false, defaultValue = "0") page: Int
    ): Page<OrderDTO> {
        log.info { "m=findByOperatorAndStatusIn, i=initiated, userCode=${user.userCode}, orderStatusList=$orderStatusList, page=$page" }
        return orderApplication.findByOperatorIdAndStatusIn(user.id!!, orderStatusList, page)
    }

    @Operation(
        summary = "Busca os detalhes de uma ordem",
        description = "Busca os detalhes de uma ordem pelo código de ordem"
    )
    @GetMapping("/{orderCode}")
    fun findByOrderCode(
        @CurrentUser user: User,
        @PathVariable orderCode: UUID
    ): OrderDetailDTO {
        log.info { "m=findByOrderCode, i=initiated, userCode=${user.userCode}, orderCode=$orderCode" }
        return orderApplication.findByOrderCode(user, orderCode)
    }

}