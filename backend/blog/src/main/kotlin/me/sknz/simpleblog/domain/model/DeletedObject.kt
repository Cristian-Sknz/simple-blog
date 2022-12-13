package me.sknz.simpleblog.domain.model

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.OffsetDateTime
import java.util.*

@Document(DeletedObject.table)
class DeletedObject : Model<UUID> {

    override lateinit var id: UUID
    lateinit var organizationId: UUID
    lateinit var collection: String

    override fun getTable(): String = table

    @Field("deleted_at")
    lateinit var deletedAt: OffsetDateTime

    companion object {
        const val table: String = "deletes"
    }
}