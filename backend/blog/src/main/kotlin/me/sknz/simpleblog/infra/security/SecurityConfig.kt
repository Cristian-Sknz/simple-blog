package me.sknz.simpleblog.infra.security

import me.sknz.simpleblog.infra.security.authentication.BearerAuthenticationConverter
import me.sknz.simpleblog.infra.security.userdetails.BlogUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerFormLoginAuthenticationConverter
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity

class SecurityConfig {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity, userDetailsService: BlogUserDetailsService, provider: JWTProvider): SecurityWebFilterChain {
        return http.cors().and().authorizeExchange()
                    .pathMatchers(HttpMethod.POST, "/auth/signup").permitAll()
                    // Permitir os métodos OPTIONS, pois a autenticação acaba bloqueando o Preflight
                    .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .anyExchange()
                    .authenticated().and()
            .addFilterAt(basicAuthenticationFilter(userDetailsService, provider), SecurityWebFiltersOrder.HTTP_BASIC)
                .csrf().disable()

                .addFilterAt(bearerAuthenticationFilter(provider), SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }

    fun basicAuthenticationFilter(userDetailsService: BlogUserDetailsService, provider: JWTProvider): AuthenticationWebFilter {
        val repository = UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService)
        val authenticationHandler = WebAuthenticationHandler(provider)
        repository.setPasswordEncoder(passwordEncoder())

        return AuthenticationWebFilter(repository).let {
            it.setAuthenticationSuccessHandler(authenticationHandler)
            it.setAuthenticationFailureHandler(authenticationHandler)
            it.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/auth/login"))
            it.setServerAuthenticationConverter(ServerFormLoginAuthenticationConverter())
            return@let it
        }
    }

    fun bearerAuthenticationFilter(provider: JWTProvider): AuthenticationWebFilter {
        return AuthenticationWebFilter(ReactiveAuthenticationManager { Mono.just(it) }).let {
            it.setServerAuthenticationConverter(BearerAuthenticationConverter(provider))
            it.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**"))
            return@let it
        }
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}