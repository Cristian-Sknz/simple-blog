package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.PostComment
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.UUID

interface PostCommentRepository: ReactiveMongoRepository<PostComment, UUID> {
}