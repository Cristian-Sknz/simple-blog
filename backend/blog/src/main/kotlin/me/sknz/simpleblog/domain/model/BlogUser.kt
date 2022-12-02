package me.sknz.simpleblog.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.OffsetDateTime
import java.util.UUID

@Document(BlogUser.table)
class BlogUser {

    @Id
    lateinit var id: UUID

    lateinit var name: String
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String

    var image: String? = null

    var organizations = arrayListOf<UUID>()

    @Field("created_at")
    lateinit var createdAt: OffsetDateTime
    @Field("updated_at")
    lateinit var updatedAt: OffsetDateTime

    companion object {
        const val table = "users"
    }
}