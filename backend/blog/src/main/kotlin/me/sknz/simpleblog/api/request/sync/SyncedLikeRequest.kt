package me.sknz.simpleblog.api.request.sync

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.domain.model.PostLike
import java.util.*

class SyncedLikeRequest: SyncedModelRequest<UUID>() {

    override lateinit var id: UUID
    @JsonProperty("user_id")
    lateinit var userId: UUID
    @JsonProperty("post_id")
    lateinit var postId: UUID
    override fun getTable() = PostLike.table

}