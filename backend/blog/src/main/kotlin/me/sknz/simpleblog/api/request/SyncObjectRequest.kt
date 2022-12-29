package me.sknz.simpleblog.api.request

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.api.request.sync.*
import me.sknz.simpleblog.api.response.sync.*
import me.sknz.simpleblog.domain.model.*
import java.util.*
import kotlin.properties.Delegates

class SyncObjectRequest {

    lateinit var changes: SyncChanges
    var lastPulledAt by Delegates.notNull<Long>()

    class SyncChanges {
        @JsonProperty(BlogPost.table)
        lateinit var posts: ChangeObject<SyncedPostRequest>
        @JsonProperty(PostComment.table)
        lateinit var comments: ChangeObject<SyncedCommentRequest>
        @JsonProperty(BlogUser.table)
        lateinit var users: ChangeObject<SyncedUserRequest>
        @JsonProperty(PostLike.table)
        lateinit var postLikes: ChangeObject<SyncedLikeRequest>
        @JsonProperty(CommentLike.table)
        lateinit var commentLikes: ChangeObject<SyncedCommentLikeRequest>

        override fun toString(): String {
            return "SyncChanges(posts=$posts, comments=$comments, users=$users, postLikes=$postLikes, commentLikes=$commentLikes)"
        }
    }

    class ChangeObject<T: SyncedModelRequest<UUID>> {
        lateinit var created: List<T>
        lateinit var updated: List<T>
        lateinit var deleted: List<UUID>
    }

    override fun toString(): String {
        return "SyncObjectRequest(changes=$changes, lastPulledAt=$lastPulledAt)"
    }
}