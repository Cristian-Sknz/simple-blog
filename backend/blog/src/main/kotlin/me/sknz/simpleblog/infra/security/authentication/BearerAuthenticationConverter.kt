package me.sknz.simpleblog.infra.security.authentication

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import me.sknz.simpleblog.infra.security.JWTProvider
import me.sknz.simpleblog.infra.security.userdetails.BlogUserDetails
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2

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
            .onErrorResume(JWTVerificationException::class.java) {
                Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT: ${it.message}", it))
            }
            .zipWith(Mono.just(exchange.request.headers))
            .map(::usernamePasswordAuthenticationToken)
    }

    private fun usernamePasswordAuthenticationToken(tuple: Tuple2<DecodedJWT, HttpHeaders>): Authentication {
        return BlogUserDetails(tuple.t1).let {
            BearerTokenAuthentication(it, it.password, it.authorities, tuple.t2.getFirst("Event-Id"))
        }
    }
}