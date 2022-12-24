package me.sknz.simpleblog.api.response.sync

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.domain.model.BlogPost
import java.util.*

data class SyncedPostResponse(
    override var id: UUID,
    val title: String,
    val subtitle: String?,
    val content: String,
    @JsonProperty("user_id")
    val userId: UUID,
    @JsonProperty("created_at")
    override val createdAt: Long,
    @JsonProperty("updated_at")
    override val updatedAt: Long,
): SyncedModel<UUID>(createdAt, updatedAt) {

    constructor(post: BlogPost) : this(
        post.id,
        post.title,
        post.subtitle,
        post.content,
        post.userId,
        post.createdAt.toInstant().toEpochMilli(),
        post.createdAt.toInstant().toEpochMilli()
    )

    override fun getTable() = BlogPost.table
}