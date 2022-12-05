package me.sknz.simpleblog.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.OffsetDateTime
import java.util.UUID

@Document(BlogPost.table)
class BlogPost: Model<UUID> {

    @Id
    override lateinit var id : UUID

    lateinit var title: String
    var subtitle: String? = null
    lateinit var content: String

    @Field("user_id")
    lateinit var userId: UUID
    @Field("organization_id")
    lateinit var organizationId: UUID

    var comments = arrayListOf<UUID>()
    var likes = arrayListOf<UUID>()

    @Field("created_at")
    lateinit var createdAt: OffsetDateTime
    @Field("updated_at")
    lateinit var updatedAt: OffsetDateTime

    override fun getTable() = table

    companion object {
        const val table = "posts"
    }
}