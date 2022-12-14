package me.sknz.simpleblog.domain.dto

import jakarta.validation.constraints.NotBlank
import me.sknz.simpleblog.domain.model.PostComment
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class CommentDTO {

    var id: UUID? = null

    @NotBlank
    lateinit var content: String

    var createdAt: OffsetDateTime? = null
    var updatedAt: OffsetDateTime? = null

    fun toPostComment(post: UUID, user: UUID): PostComment {
        val comment = PostComment()
        comment.id = this.id ?: UUID.randomUUID()
        comment.postId = post
        comment.userId = user
        comment.content = this.content
        comment.createdAt = this.createdAt ?: OffsetDateTime.now(ZoneOffset.UTC)
        comment.updatedAt = this.updatedAt ?: OffsetDateTime.now(ZoneOffset.UTC)
        return comment
    }
}