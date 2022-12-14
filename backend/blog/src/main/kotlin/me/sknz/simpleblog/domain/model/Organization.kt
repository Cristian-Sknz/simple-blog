package me.sknz.simpleblog.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.OffsetDateTime
import java.util.UUID

@Document(Organization.table)
class Organization: Model<UUID> {

    @Id
    override lateinit var id: UUID
    lateinit var name: String

    @Field("owner_id")
    lateinit var ownerId: UUID

    var members = arrayListOf<UUID>()
    var invites = arrayListOf<UUID>()

    @Field("created_at")
    lateinit var createdAt: OffsetDateTime
    @Field("updated_at")
    lateinit var updatedAt: OffsetDateTime

    var public : Boolean = false

    override fun getTable() = table

    companion object {
        const val table = "organizations"
    }
}