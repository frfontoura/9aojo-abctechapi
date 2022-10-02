package br.com.fiap.abctechapi.config.security

import br.com.fiap.abctechapi.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl: UserDetails {

    private val user: User
    private val username: String
    private val authorities: Collection<GrantedAuthority>
    private val password: String

    constructor(user: User) {
        this.user = user
        this.username = user.username
        this.password = user.password
        this.authorities = user.roles.map { SimpleGrantedAuthority(it.name.name) }
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun getUser(): User {
        return user
    }

}