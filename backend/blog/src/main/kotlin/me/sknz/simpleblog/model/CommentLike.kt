package me.sknz.simpleblog.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.OffsetDateTime
import java.util.UUID

@Document(CommentLike.table)
class CommentLike {

    @Id
    lateinit var id: UUID

    @Field("comment_id")
    lateinit var commentId: UUID
    @Field("user_id")
    lateinit var userId: UUID

    @Field("created_at")
    lateinit var createdAt: OffsetDateTime
    @Field("updated_at")
    lateinit var updatedAt: OffsetDateTime

    companion object {
        const val table = "comment_likes"
    }
}