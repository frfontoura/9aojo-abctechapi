package br.com.fiap.abctechapi.model

import br.com.fiap.abctechapi.enums.OrderLocationType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "orders_locations")
data class OrderLocation(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orders_locations", nullable = false)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orders")
    val order: Order,

    @Column(name = "cod_order_location", nullable = false)
    val orderLocationCode: UUID = UUID.randomUUID(),

    @Column(name = "num_latitude", nullable = false)
    val latitude: BigDecimal,

    @Column(name = "num_longitude", nullable = false)
    val longitude: BigDecimal,

    @Column(name = "dat_order", nullable = false)
    val date: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "ind_location_type", nullable = false)
    val type: OrderLocationType
)
