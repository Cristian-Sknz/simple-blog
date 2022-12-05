package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.CommentLike
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import java.util.UUID

interface CommentLikeRepository: ReactiveMongoRepository<CommentLike, UUID> {
    
    fun findByCommentIdAndUserId(commentId: UUID, userId: UUID): Mono<CommentLike>

    fun existsByCommentIdAndUserId(commentId: UUID, userId: UUID): Mono<Boolean>

}