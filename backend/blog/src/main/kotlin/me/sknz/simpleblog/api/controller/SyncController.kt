package me.sknz.simpleblog.api.controller

import me.sknz.simpleblog.api.response.SyncResponse
import me.sknz.simpleblog.domain.service.SyncService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@RestController
@RequestMapping("/api/organizations/{organization}")
class SyncController(
    private val sync: SyncService
) {

    @GetMapping(path = ["sync"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun pull(@PathVariable organization: UUID,
            @RequestParam parameters: Map<String, String>): Mono<SyncResponse> {
        val timestamp = parameters["last_pulled_at"] ?: "0"
        /*
        Ignorar a versão do esquema, não irei implementar
        pois, não é necessário para este projeto.

        `val schema = parameters["schema_version"]`
        */

        val date = timestamp.let {
            if (it == "0") {
                return@let null
            }
            Instant.ofEpochMilli(it.toLong() - 1)
                .atOffset(ZoneOffset.UTC)
        }

        return Mono.just(OffsetDateTime.now(ZoneOffset.UTC))
            .flatMap { sync.pullChanges(organization, date).map { changes -> SyncResponse(changes, it) } }
    }

    @PostMapping(path = ["sync"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun push(@PathVariable organization: UUID): Mono<Any> {
        TODO("Ainda não foi implementado.")
    }
}