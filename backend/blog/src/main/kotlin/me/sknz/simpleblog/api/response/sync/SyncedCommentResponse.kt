package me.sknz.simpleblog.api.response.sync

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.domain.model.PostComment
import java.util.*

data class SyncedCommentResponse(
    override var id: UUID,
    val content: String,
    @JsonProperty("user_id")
    val userId: UUID,
    @JsonProperty("post_id")
    val postId: UUID,
    @JsonProperty("created_at")
    override val createdAt: Long,
    @JsonProperty("updated_at")
    override val updatedAt: Long,
): SyncedModel<UUID>(createdAt, updatedAt) {

    constructor(comment: PostComment) : this(
        comment.id,
        comment.content,
        comment.userId,
        comment.postId,
        comment.createdAt.toInstant().toEpochMilli(),
        comment.createdAt.toInstant().toEpochMilli()
    )

    override fun getTable() = PostComment.table
}