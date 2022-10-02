package br.com.fiap.abctechapi.model

import br.com.fiap.abctechapi.enums.OrderLocationType
import br.com.fiap.abctechapi.enums.OrderStatus
import br.com.fiap.abctechapi.exceptions.BusinessException
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "orders")
data class Order(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orders", nullable = false)
    val id: Long? = null,

    @Column(name = "cod_order", nullable = false)
    val orderCode: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    @Column(name = "ind_status", nullable = false)
    var status: OrderStatus = OrderStatus.STARTED,

    @ManyToOne
    @JoinColumn(name = "id_users")
    val operator: User,

    @ManyToMany
    @JoinTable(name = "orders_assists", joinColumns = [JoinColumn(name = "id_orders")], inverseJoinColumns = [JoinColumn(name = "id_assists")])
    val assists: List<Assistance>

) {

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val orderLocations: MutableList<OrderLocation> = mutableListOf()

    fun addOrderLocation(orderLocation: OrderLocation) {
        getOrderLocationByType(orderLocation.type)?.also {
            throw BusinessException(message = "Tipo de orderLocation j\u00E1 existente")
        }
        orderLocations.add(orderLocation)
    }

    fun getOrderLocationByType(type: OrderLocationType): OrderLocation? {
        return orderLocations.find { type == it.type }
    }

    fun validateFinalizeOrder() {
        if (getOrderLocationByType(OrderLocationType.START) == null) {
            throw BusinessException(
                message = "Status da ordem inv\u00E1lida",
                description = "A ordem precisa ser inicializada antes de ser finalizada"
            )
        }

        if (OrderStatus.FINISHED == status) {
            throw BusinessException(
                message = "Ordem j\u00E1 finalizada",
                description = "A ordem j\u00E1 foi finalizada"
            )
        }
    }

}