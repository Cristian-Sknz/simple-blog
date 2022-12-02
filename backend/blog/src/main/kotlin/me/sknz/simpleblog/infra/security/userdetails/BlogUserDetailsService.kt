package me.sknz.simpleblog.infra.security.userdetails

import me.sknz.simpleblog.domain.model.BlogUser
import me.sknz.simpleblog.infra.repository.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class BlogUserDetailsService(
    private val repository: UserRepository
) : ReactiveUserDetailsService {

    override fun findByUsername(username: String): Mono<UserDetails> {
        return repository.findByUsernameOrEmail(username)
            .switchIfEmpty {
                Mono.error(UsernameNotFoundException("Usuário não foi encontrado"))
            }.map(::BlogUserDetails)
    }

    private fun UserRepository.findByUsernameOrEmail(usernameOrEmail: String): Mono<BlogUser> {
        if (usernameOrEmail.contains('@')) {
            this.findByEmail(usernameOrEmail)
        }
        return this.findByUsername(usernameOrEmail)
    }
}