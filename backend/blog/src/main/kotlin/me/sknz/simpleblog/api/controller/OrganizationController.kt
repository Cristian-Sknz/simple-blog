package me.sknz.simpleblog.api.controller

import me.sknz.simpleblog.domain.model.Organization
import me.sknz.simpleblog.domain.service.EmitterService
import me.sknz.simpleblog.domain.service.ModelEmitter
import me.sknz.simpleblog.domain.service.OrganizationService
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/api/organizations")
class OrganizationController(
    emitter: EmitterService,
    val organizations: OrganizationService
): ModelEmitter(emitter) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getOrganizations(): Flux<Organization> {
        return organizations.getOrganizations()
    }

    @GetMapping(path = ["/{organization}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getOrganization(@PathVariable organization: UUID): Mono<Organization> {
        return organizations.getOrganization(organization)
    }

    @GetMapping(value = ["/{organization}/changes"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun changes(@PathVariable organization: UUID): Flux<ServerSentEvent<Any?>> {
        return emitter.getOrganizationRealtimeChanges(organization).map {
            ServerSentEvent.builder(it.payload as Any)
                .id(UUID.randomUUID().toString())
                .event(it.type)
                .build()
        }.mergeWith(
            Flux.just(
                ServerSentEvent.builder<Any>(emptyList<Any>())
                    .id("join")
                    .event("join")
                    .build()
            )
        )
    }
}