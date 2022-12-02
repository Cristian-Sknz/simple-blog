package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.PostLike
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.UUID

interface PostLikeRepository: ReactiveMongoRepository<PostLike, UUID> {
}