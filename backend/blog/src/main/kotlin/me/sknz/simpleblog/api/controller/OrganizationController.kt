package me.sknz.simpleblog.api.controller

import jakarta.validation.Valid
import me.sknz.simpleblog.domain.dto.OrganizationDTO
import me.sknz.simpleblog.domain.dto.OrganizationInviteDTO
import me.sknz.simpleblog.domain.model.Organization
import me.sknz.simpleblog.domain.service.EmitterService
import me.sknz.simpleblog.domain.service.InviteService
import me.sknz.simpleblog.domain.service.ModelEmitter
import me.sknz.simpleblog.domain.service.OrganizationService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.util.*

@RestController
@RequestMapping("/api/organizations")
class OrganizationController(
    emitter: EmitterService,
    val organizations: OrganizationService,
    val invites: InviteService
) : ModelEmitter(emitter) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getOrganizations(): Flux<Organization> {
        return organizations.getOrganizations()
    }

    @GetMapping(path = ["/public"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getPublicOrganizations(): Flux<Organization> {
        return organizations.getPublicOrganizations()
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrganization(@Valid @RequestBody dto: OrganizationDTO): Mono<Organization> {
        return organizations.createOrganization(dto)
    }

    @GetMapping(path = ["/{organization}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getOrganization(@PathVariable organization: UUID): Mono<Organization> {
        return organizations.getOrganization(organization)
    }

    @GetMapping(path = ["/{organization}/join"])
    @ResponseStatus(HttpStatus.OK)
    fun joinPublicOrganization(organization: UUID): Mono<Void> {
        return invites.joinPublicOrganization(organization)
    }

    @PostMapping(path = ["/{organization}/leave"])
    @ResponseStatus(HttpStatus.OK)
    fun leaveFromOrganization(@PathVariable organization: UUID): Mono<Void> {
        return invites.leaveOrganization(organization)
    }

    @GetMapping(path = ["/{organization}/invite"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getInvites(@PathVariable organization: UUID): Flux<OrganizationInviteDTO> {
        return organizations.getOrganization(organization).flatMapMany { org ->
            org.invites.toFlux().map { OrganizationInviteDTO(it, org.id) }
        }
    }

    @PostMapping(path = ["/{organization}/invite"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createInvite(@PathVariable organization: UUID): Mono<OrganizationInviteDTO> {
        return invites.createInvite(organization)
    }

    @DeleteMapping(path = ["/{organization}/invite/{invite}"])
    @ResponseStatus(HttpStatus.OK)
    fun removeInvite(@PathVariable organization: UUID,
                     @PathVariable invite: UUID): Mono<Void> {
        return invites.removeInvite(organization, invite)
    }

    @GetMapping(value = ["/{organization}/changes"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun changes(@PathVariable organization: UUID): Flux<ServerSentEvent<Any?>> {
        return emitter.getOrganizationRealtimeChanges(organization).map {
            ServerSentEvent.builder(it.payload as Any)
                .id(UUID.randomUUID().toString())
                .event(it.type)
                .build()
        }.mergeWith(ServerSentEvent.builder<Any>(emptyList<Any>()).id("join").event("join")
            .build().toMono())
    }
}