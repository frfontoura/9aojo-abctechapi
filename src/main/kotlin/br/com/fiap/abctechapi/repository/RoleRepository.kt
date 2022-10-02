package br.com.fiap.abctechapi.repository

import br.com.fiap.abctechapi.enums.RoleType
import br.com.fiap.abctechapi.model.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role, Long> {

    fun findByName(roleType: RoleType): Role?

}