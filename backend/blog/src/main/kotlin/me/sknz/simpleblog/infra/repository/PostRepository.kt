package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.BlogPost
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.UUID

interface PostRepository: ReactiveMongoRepository<BlogPost, UUID> {

    fun findByOrganizationId(organizationId: UUID): Flux<BlogPost>
    
    fun findByOrganizationIdAndId(organizationId: UUID, id: UUID): Mono<BlogPost>

    fun findByOrganizationIdAndCreatedAtAfter(organizationId: Any, createdAt: OffsetDateTime): Flux<BlogPost>

}