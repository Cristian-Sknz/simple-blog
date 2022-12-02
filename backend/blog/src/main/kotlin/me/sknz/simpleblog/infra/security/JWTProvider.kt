package me.sknz.simpleblog.infra.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.validation.constraints.NotEmpty
import me.sknz.simpleblog.infra.security.userdetails.BlogUserDetails
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.time.OffsetDateTime
import java.util.*

@Component
@Validated
@ConfigurationProperties("authentication.jwt")
class JWTProvider {

    @NotEmpty
    lateinit var expires: String
    @NotEmpty
    lateinit var clientSecret: String

    fun generate(principal: BlogUserDetails): TokenResponse {
        return JWT.create().withSubject(principal.id.toString())
            .withClaim("email", principal.email)
            .withClaim("username", principal.username)
            .withClaim("authorities", principal.authorities.map { it.authority })
            .withIssuedAt(Date.from(OffsetDateTime.now().toInstant()))
            .withExpiresAt(Date.from(OffsetDateTime.now().plusSeconds(expires.toLong()).toInstant()))
            .toTokenResponse(expires.toLong())
    }

    fun decode(token: String): DecodedJWT {
        return JWT.require(Algorithm.HMAC512(clientSecret))
            .build().verify(token)
    }

    private fun JWTCreator.Builder.sign(): String {
        return this.sign(Algorithm.HMAC512(clientSecret))
    }

    private fun JWTCreator.Builder.toTokenResponse(expires: Long): TokenResponse {
        return TokenResponse(expires, this.sign())
    }

    data class TokenResponse(
        val expires: Long,
        val token: String,
        val type: String = "Bearar"
    )
}