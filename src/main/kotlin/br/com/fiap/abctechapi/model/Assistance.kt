package br.com.fiap.abctechapi.model

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "assists")
data class Assistance(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_assists", nullable = false)
    var id: Long? = null,

    @Column(name = "nam_assistance", nullable = false, length = 100)
    val name: String,

    @Column(name = "des_assistance", nullable = false, length = 300)
    val description: String,

    @Column(name = "cod_assistance", nullable = false)
    val assistanceCode: UUID = UUID.randomUUID()

)
