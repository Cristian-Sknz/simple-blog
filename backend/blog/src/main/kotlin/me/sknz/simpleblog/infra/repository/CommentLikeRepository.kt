package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.CommentLike
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.UUID

interface CommentLikeRepository: ReactiveMongoRepository<CommentLike, UUID> {
    
    fun findByCommentIdAndUserId(commentId: UUID, userId: UUID): Mono<CommentLike>
    fun existsByCommentIdAndUserId(commentId: UUID, userId: UUID): Mono<Boolean>
    fun findAllByCommentIdIn(commentIds: Flux<UUID>): Flux<CommentLike>
    fun findAllByCommentIdInAndUpdatedAtAfter(ids: Flux<UUID>, timestamp: OffsetDateTime): Flux<CommentLike>
    fun findAllByCommentIdInAndCreatedAtAfter(ids: Flux<UUID>, timestamp: OffsetDateTime): Flux<CommentLike>

}