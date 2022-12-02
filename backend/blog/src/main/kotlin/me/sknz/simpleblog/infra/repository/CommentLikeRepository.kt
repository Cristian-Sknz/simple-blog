package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.CommentLike
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.UUID

interface CommentLikeRepository: ReactiveMongoRepository<CommentLike, UUID> {
}