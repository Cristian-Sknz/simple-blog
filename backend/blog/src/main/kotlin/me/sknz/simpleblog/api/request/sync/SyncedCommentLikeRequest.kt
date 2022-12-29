package me.sknz.simpleblog.api.request.sync

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.domain.model.CommentLike
import java.util.*

class SyncedCommentLikeRequest : SyncedModelRequest<UUID>() {

    override lateinit var id: UUID
    @JsonProperty("user_id")
    lateinit var userId: UUID
    @JsonProperty("comment_id")
    lateinit var commentId: UUID

    override fun getTable() = CommentLike.table
}