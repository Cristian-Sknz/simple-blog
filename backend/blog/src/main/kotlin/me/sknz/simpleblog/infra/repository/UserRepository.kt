package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.BlogUser
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.*

interface UserRepository : ReactiveMongoRepository<BlogUser, UUID> {

    fun findByEmail(email: String): Mono<BlogUser>
    fun findByUsername(username: String): Mono<BlogUser>
    

    fun existsByEmail(email: String): Mono<Boolean>
    fun existsByUsername(username: String): Mono<Boolean>


    fun findByOrganizationsContains(organizationId: UUID): Flux<BlogUser>
    fun findByOrganizationsContainsAndCreatedAtAfter(organizationId: Any, createdAt: OffsetDateTime): Flux<BlogUser>
    fun findByOrganizationsContainsAndUpdatedAtAfter(organizationId: Any, createdAt: OffsetDateTime): Flux<BlogUser>

}