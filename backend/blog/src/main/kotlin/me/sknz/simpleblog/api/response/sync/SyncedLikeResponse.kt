package me.sknz.simpleblog.api.response.sync

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.domain.model.PostLike
import java.util.*

data class SyncedLikeResponse(
    override var id: UUID,
    @JsonProperty("user_id")
    val userId: UUID,
    @JsonProperty("post_id")
    val postId: UUID,
    @JsonProperty("created_at")
    override val createdAt: Long,
    @JsonProperty("updated_at")
    override val updatedAt: Long,
): SyncedModel<UUID>(createdAt, updatedAt) {

    constructor(like: PostLike) : this(
        like.id,
        like.userId,
        like.postId,
        like.createdAt.toInstant().toEpochMilli(),
        like.createdAt.toInstant().toEpochMilli()
    )

    override fun getTable() = PostLike.table
}