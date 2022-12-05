package me.sknz.simpleblog.api.controller

import me.sknz.simpleblog.domain.event.OrganizationDatabaseEvent
import me.sknz.simpleblog.domain.model.Organization
import me.sknz.simpleblog.domain.service.EmitterService
import me.sknz.simpleblog.domain.service.OrganizationService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
@RequestMapping("/api/organizations")
class OrganizationController(
    val emitter: EmitterService,
    val organizations: OrganizationService
) {

    @GetMapping
    fun getOrganizations(): Flux<Organization> {
        return organizations.getOrganizations()
    }

    @GetMapping(path = ["/{organization}"])
    fun getOrganization(@PathVariable organization: UUID): Mono<Organization> {
        return organizations.getOrganization(organization)
    }

    @GetMapping(value = ["/{organization}/changes"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun changes(@PathVariable organization: UUID): Flux<OrganizationDatabaseEvent<*>> {
        return emitter.getOrganizationRealtimeChanges(organization)
    }
}