package me.sknz.simpleblog.infra.security

import com.auth0.jwt.interfaces.DecodedJWT
import me.sknz.simpleblog.infra.security.userdetails.BlogUserDetails
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class BearerAuthenticationConverter(
    private val provider: JWTProvider
): ServerAuthenticationConverter {

    companion object {
        const val BEARER = "Bearer "
    }

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        return Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
            .filter { it.startsWith(BEARER) }
            .map { it.substring(BEARER.length) }
            .map(provider::decode)
            .map(::usernamePasswordAuthenticationToken)
    }

    private fun usernamePasswordAuthenticationToken(user: DecodedJWT): Authentication {
        return BlogUserDetails(user).let { UsernamePasswordAuthenticationToken(it, it.password, it.authorities)}
    }
}