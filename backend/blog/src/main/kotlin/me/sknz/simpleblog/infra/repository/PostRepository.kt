package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.BlogPost
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.UUID

interface PostRepository: ReactiveMongoRepository<BlogPost, UUID> {
}