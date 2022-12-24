package me.sknz.simpleblog.api.response

import java.time.OffsetDateTime

data class SyncResponse(val changes: Map<String, Any>, val date: OffsetDateTime) {
    val timestamp = date.toInstant().toEpochMilli()
}