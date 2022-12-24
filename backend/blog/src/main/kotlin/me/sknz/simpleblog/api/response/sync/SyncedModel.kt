package me.sknz.simpleblog.api.response.sync

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.simpleblog.domain.model.Model
import java.time.Instant
import java.time.ZoneOffset

abstract class SyncedModel<T>(
    @JsonProperty("created_at")
    open val createdAt: Long,
    @JsonProperty("updated_at")
    open val updatedAt: Long
) : Model<T> {

    @JsonProperty("created_at_utc")
    fun getCreatedAtUTC() = Instant.ofEpochMilli(this.createdAt).atOffset(ZoneOffset.UTC)
    @JsonProperty("updated_at_utc")
    fun getUpdatedAtUTC() = Instant.ofEpochMilli(this.createdAt).atOffset(ZoneOffset.UTC)
}