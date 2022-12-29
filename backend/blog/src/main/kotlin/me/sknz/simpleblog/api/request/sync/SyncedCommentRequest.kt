package me.sknz.simpleblog.api.request.sync

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.domain.model.PostComment
import java.util.*

class SyncedCommentRequest: SyncedModelRequest<UUID>() {

    override lateinit var id: UUID
    lateinit var content: String
    @JsonProperty("user_id")
    lateinit var userId: UUID
    @JsonProperty("post_id")
    lateinit var postId: UUID

    override fun getTable() = PostComment.table
}