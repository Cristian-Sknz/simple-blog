package me.sknz.simpleblog.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import me.sknz.simpleblog.api.response.sync.SyncedUserResponse
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.OffsetDateTime
import java.util.UUID

@Document(BlogUser.table)
class BlogUser: Model<UUID>{

    @Id
    override lateinit var id: UUID

    lateinit var name: String
    lateinit var username: String
    lateinit var email: String
    @JsonIgnore
    lateinit var password: String

    var image: String? = null

    var organizations = arrayListOf<UUID>()

    @Field("created_at")
    lateinit var createdAt: OffsetDateTime
    @Field("updated_at")
    lateinit var updatedAt: OffsetDateTime

    override fun getTable() = table

    override fun toResponseModel() = SyncedUserResponse(this)

    companion object {
        const val table = "users"
    }
}