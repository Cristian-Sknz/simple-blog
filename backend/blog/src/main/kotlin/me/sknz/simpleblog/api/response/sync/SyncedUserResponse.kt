package me.sknz.simpleblog.api.response.sync

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.domain.model.BlogUser
import java.util.*

data class SyncedUserResponse(
    override var id: UUID,
    val name: String,
    val username: String,
    val email: String,
    val image: String?,
    @JsonProperty("created_at")
    override val createdAt: Long,
    @JsonProperty("updated_at")
    override val updatedAt: Long,
): SyncedModelResponse<UUID>(createdAt, updatedAt) {

    constructor(user: BlogUser) : this(
        user.id,
        user.name,
        user.username,
        user.email,
        user.image,
        user.createdAt.toInstant().toEpochMilli(),
        user.updatedAt.toInstant().toEpochMilli()
    )

    override fun getTable() = BlogUser.table
}