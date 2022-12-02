package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.BlogUser
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import java.util.*

interface UserRepository : ReactiveMongoRepository<BlogUser, UUID> {

    fun findByEmail(email: String): Mono<BlogUser>
    fun findByUsername(username: String): Mono<BlogUser>

}