package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.PostLike
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.UUID

interface PostLikeRepository: ReactiveMongoRepository<PostLike, UUID> {

    fun findByPostIdAndUserId(postId: UUID, userId: UUID): Mono<PostLike>
    fun findAllByIdAndCreatedAtAfter(ids: Flux<UUID>, timestamp: OffsetDateTime): Flux<PostLike>
    fun findAllByIdAndUpdatedAtAfter(ids: Flux<UUID>, timestamp: OffsetDateTime): Flux<PostLike>

}