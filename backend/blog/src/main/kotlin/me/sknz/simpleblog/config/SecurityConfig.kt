package me.sknz.simpleblog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.cors.reactive.CorsWebFilter

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.authorizeExchange()
            .anyExchange().permitAll()
                .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .build()
    }

    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val configuration = CorsConfiguration()
        val source = UrlBasedCorsConfigurationSource()
        configuration.allowCredentials = true
        configuration.addAllowedHeader("*")
        configuration.addAllowedMethod("*")
        configuration.allowedOriginPatterns = listOf("*")
        configuration.maxAge = 3600L
        source.registerCorsConfiguration("/**", configuration)
        return CorsWebFilter(source)
    }
}