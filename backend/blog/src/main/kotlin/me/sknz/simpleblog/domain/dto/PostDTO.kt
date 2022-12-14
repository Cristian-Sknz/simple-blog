package me.sknz.simpleblog.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import me.sknz.simpleblog.domain.model.BlogPost
import java.time.OffsetDateTime
import java.util.*

class PostDTO {

    var id : UUID? = null

    @NotBlank
    lateinit var title: String
    var subtitle: String? = null
    @NotBlank
    lateinit var content: String

    @JsonProperty("user_id")
    lateinit var userId: UUID
    @JsonProperty("created_at")
    var createdAt: OffsetDateTime? = null
    @JsonProperty("updated_at")
    var updatedAt: OffsetDateTime? = null

    fun toNewBlogPost(): BlogPost {
        val post = BlogPost()
        post.id = this.id ?: UUID.randomUUID()
        post.title = this.title
        post.subtitle = this.subtitle
        post.content = this.content
        post.createdAt = this.createdAt ?: OffsetDateTime.now()
        post.updatedAt = this.updatedAt ?: OffsetDateTime.now()
        return post
    }
}