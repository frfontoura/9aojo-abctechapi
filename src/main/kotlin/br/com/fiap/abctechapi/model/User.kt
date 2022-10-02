package br.com.fiap.abctechapi.model

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users", nullable = false)
    var id: Long? = null,

    @Column(name = "nam_username", nullable = false)
    val username: String,

    @Column(name = "des_email", nullable = false)
    val email: String,

    @Column(name = "des_password", nullable = false)
    val password: String,

    @Column(name = "cod_user", nullable = false)
    val userCode: UUID = UUID.randomUUID(),

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = [JoinColumn(name = "id_users")], inverseJoinColumns = [JoinColumn(name = "id_roles")])
    val roles: Set<Role> = mutableSetOf()
)
