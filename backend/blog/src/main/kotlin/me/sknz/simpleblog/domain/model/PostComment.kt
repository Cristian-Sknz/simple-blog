package me.sknz.simpleblog.domain.model

import me.sknz.simpleblog.api.response.sync.SyncedCommentResponse
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.OffsetDateTime
import java.util.UUID

@Document(PostComment.table)
class PostComment: Model<UUID> {

    @Id
    override lateinit var id: UUID

    lateinit var content: String
    @Field("post_id")
    lateinit var postId: UUID
    @Field("user_id")
    lateinit var userId: UUID

    var likes = arrayListOf<UUID>()

    @Field("created_at")
    lateinit var createdAt: OffsetDateTime
    @Field("updated_at")
    lateinit var updatedAt: OffsetDateTime

    override fun getTable() = table

    override fun toResponseModel() = SyncedCommentResponse(this)

    companion object {
        const val table = "comments"
    }
}