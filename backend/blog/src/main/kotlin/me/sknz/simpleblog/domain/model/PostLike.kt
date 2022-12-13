package me.sknz.simpleblog.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.OffsetDateTime
import java.util.UUID

@Document(PostLike.table)
class PostLike : Model<UUID> {

    @Id
    override lateinit var id: UUID

    @Field("post_id")
    lateinit var postId: UUID
    @Field("user_id")
    lateinit var userId: UUID

    @Field("created_at")
    lateinit var createdAt: OffsetDateTime
    @Field("updated_at")
    lateinit var updatedAt: OffsetDateTime

    override fun getTable() = table

    companion object {
        fun from(post: UUID, user: UUID): PostLike {
            val like = PostLike()
            like.postId = post
            like.userId = user
            like.id = UUID.randomUUID()
            like.createdAt = OffsetDateTime.now()
            like.updatedAt = OffsetDateTime.now()
            return like
        }

        const val table = "post_likes"
    }
}