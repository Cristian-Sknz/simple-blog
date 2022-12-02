package me.sknz.simpleblog.infra.security

import com.fasterxml.jackson.databind.ObjectMapper
import me.sknz.simpleblog.infra.security.userdetails.BlogUserDetails
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class WebAuthenticationHandler(
    private val provider: JWTProvider
) : ServerAuthenticationFailureHandler, ServerAuthenticationSuccessHandler {

    private val mapper = ObjectMapper()
    private val factory = DefaultDataBufferFactory()

    override fun onAuthenticationFailure(webFilterExchange: WebFilterExchange, exception: AuthenticationException): Mono<Void> {
        return Mono.fromRunnable {
            val exchange = webFilterExchange.exchange
            val response = exchange.response
            val node = mapper.createObjectNode()

            node.put("path", exchange.request.path.value())
            node.put("status", response.statusCode!!.value())
            node.put("message", exception.message)
            node.put("timestamp", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))

            val dataBuffer = factory.wrap(node.toPrettyString().toByteArray())
            response.statusCode = HttpStatus.BAD_REQUEST
            response.headers.contentType = MediaType.APPLICATION_JSON
            response.writeWith(Mono.just(dataBuffer)).subscribe()
            response.setComplete().subscribe()
        }
    }

    override fun onAuthenticationSuccess(webFilterExchange: WebFilterExchange, authentication: Authentication): Mono<Void> {
        return Mono.fromRunnable {
            val exchange = webFilterExchange.exchange
            val response = exchange.response

            val token = provider.generate(authentication.principal as BlogUserDetails)
            val dataBuffer = factory.wrap(mapper.writeValueAsString(token).toByteArray())

            response.statusCode = HttpStatus.OK
            response.headers.contentType = MediaType.APPLICATION_JSON
            response.writeWith(Mono.just(dataBuffer)).subscribe()

            response.setComplete().subscribe()
        }
    }
}