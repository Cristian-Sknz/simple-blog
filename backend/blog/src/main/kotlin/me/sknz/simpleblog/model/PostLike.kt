package me.sknz.simpleblog.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.OffsetDateTime
import java.util.UUID

@Document(PostLike.table)
class PostLike {

    @Id
    lateinit var id: UUID

    @Field("post_id")
    lateinit var postId: UUID
    @Field("user_id")
    lateinit var userId: UUID

    @Field("created_at")
    lateinit var createdAt: OffsetDateTime
    @Field("updated_at")
    lateinit var updatedAt: OffsetDateTime

    companion object {
        const val table = "post_likes"
    }
}