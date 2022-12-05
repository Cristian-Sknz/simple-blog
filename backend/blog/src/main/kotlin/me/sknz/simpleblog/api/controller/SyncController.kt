package me.sknz.simpleblog.api.controller

import me.sknz.simpleblog.domain.service.SyncService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

@RestController
@RequestMapping("/api/organizations/{organization}")
class SyncController(
    private val sync: SyncService
) {

    /* Ainda não está completa */
    @GetMapping(path = ["sync"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun pull(@PathVariable organization: UUID,
            @RequestParam parameters: Map<String, String>): Mono<out Map<String, Any>> {
        val timestamp = parameters["last_pulled_at"] ?: "0"
        val schema = parameters["schema_version"]

        if (schema == "1") {
            val date = timestamp.let {
                if (it == "0") {
                    return@let null
                }
                Instant.ofEpochMilli(it.toLong() - 1)
                    .atOffset(ZoneOffset.UTC)
            }

            return sync.pullChanges(organization, date)
        }

        return Mono.error(RuntimeException())
    }
}