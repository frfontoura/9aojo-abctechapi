package br.com.fiap.abctechapi.model

import br.com.fiap.abctechapi.enums.RoleType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "roles")
data class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_roles", nullable = false)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "nam_role", nullable = false)
    val name: RoleType
)
