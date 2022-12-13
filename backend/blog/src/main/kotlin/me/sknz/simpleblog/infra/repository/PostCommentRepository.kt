package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.PostComment
import org.reactivestreams.Publisher
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.UUID

interface PostCommentRepository: ReactiveMongoRepository<PostComment, UUID> {

    fun findByPostId(postId: UUID): Flux<PostComment>
    fun findByPostIdAndId(postId: UUID, id: UUID): Mono<PostComment>

    fun findAllByIdAndCreatedAtAfter(idStream: Publisher<UUID>, createdAt: OffsetDateTime): Flux<PostComment>
    fun findAllByIdAndUpdatedAtAfter(idStream: Publisher<UUID>, createdAt: OffsetDateTime): Flux<PostComment>

}