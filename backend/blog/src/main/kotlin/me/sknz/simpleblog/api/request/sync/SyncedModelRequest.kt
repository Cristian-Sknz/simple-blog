package me.sknz.simpleblog.api.request.sync

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.domain.model.Model
import java.time.Instant
import java.time.ZoneOffset
import kotlin.properties.Delegates

abstract class SyncedModelRequest<T> : Model<T> {

    @get:JsonProperty("created_at")
    open var createdAt by Delegates.notNull<Long>()

    @get:JsonProperty("updated_at")
    open var updatedAt by Delegates.notNull<Long>()

    @JsonProperty("created_at_utc")
    fun getCreatedAtUTC() = Instant.ofEpochMilli(this.createdAt).atOffset(ZoneOffset.UTC)
    @JsonProperty("updated_at_utc")
    fun getUpdatedAtUTC() = Instant.ofEpochMilli(this.createdAt).atOffset(ZoneOffset.UTC)

    override fun toString(): String {
        return "SyncedModelRequest(createdAt=$createdAt, updatedAt=$updatedAt)"
    }


}