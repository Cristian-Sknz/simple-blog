package me.sknz.simpleblog.api.request.sync

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.domain.model.BlogPost
import java.util.*

class SyncedPostRequest: SyncedModelRequest<UUID>() {

    override lateinit var id: UUID
    lateinit var title: String
    var subtitle: String? = null
    lateinit var content: String
    @JsonProperty("user_id")
    lateinit var userId: UUID

    override fun getTable() = BlogPost.table



    fun toBlogPost(organization: UUID): BlogPost {
        return BlogPost().let {
            it.id = this.id
            it.title = this.title
            it.subtitle = this.subtitle
            it.content = this.content
            it.organizationId = organization
            it.userId = this.userId
            it.createdAt = this.getCreatedAtUTC()
            it.updatedAt = this.getUpdatedAtUTC()
            it
        }
    }

    override fun toString(): String {
        return "SyncedPostRequest(id=$id, title='$title', subtitle=$subtitle, content='$content', userId=$userId) ${super.toString()}"
    }
}