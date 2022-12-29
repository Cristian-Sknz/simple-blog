package me.sknz.simpleblog.api.response.sync

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.domain.model.CommentLike
import java.util.*

data class SyncedCommentLikeResponse(
    override var id: UUID,
    @JsonProperty("user_id")
    val userId: UUID,
    @JsonProperty("comment_id")
    val commentId: UUID,
    @JsonProperty("created_at")
    override val createdAt: Long,
    @JsonProperty("updated_at")
    override val updatedAt: Long,
): SyncedModelResponse<UUID>(createdAt, updatedAt) {

    constructor(like: CommentLike) : this(
        like.id,
        like.userId,
        like.commentId,
        like.createdAt.toInstant().toEpochMilli(),
        like.createdAt.toInstant().toEpochMilli()
    )

    override fun getTable() = CommentLike.table
}