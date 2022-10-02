package br.com.fiap.abctechapi.config.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import mu.KLogger
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@Component
class JwtUtil(
    @Value("\${config.jwt.secret}")
    private val jwtSecret: String,

    @Value("\${config.jwt.expire}")
    private val jwtExpiration: Int
) {

    private val log: KLogger = KotlinLogging.logger { }

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal: UserDetailsImpl = authentication.principal as UserDetailsImpl
        val expiration = Date.from(LocalDateTime.now().plusMinutes(jwtExpiration.toLong()).atZone(ZoneId.systemDefault()).toInstant())

        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getUserNameFromJwtToken(token: String): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            log.error { "Invalid JWT signature: ${e.message}" }
        } catch (e: MalformedJwtException) {
            log.error { "Invalid JWT token: ${e.message}" }
        } catch (e: ExpiredJwtException) {
            log.error { "JWT token is expired: ${e.message}" }
        } catch (e: UnsupportedJwtException) {
            log.error { "JWT token is unsupported: ${e.message}" }
        } catch (e: IllegalArgumentException) {
            log.error { "JWT claims string is empty: ${e.message}" }
        }
        return false
    }

}