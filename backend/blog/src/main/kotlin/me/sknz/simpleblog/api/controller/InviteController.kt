package me.sknz.simpleblog.api.controller

import me.sknz.simpleblog.domain.model.Organization
import me.sknz.simpleblog.domain.service.InviteService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
@RequestMapping("/api/invites")
class InviteController(
    val invites: InviteService
) {
    @PostMapping("/{invite}/join")
    @ResponseStatus(HttpStatus.OK)
    fun join(@PathVariable invite: UUID): Mono<Void> {
        return invites.joinOrganization(invite)
    }

    @GetMapping("/{invite}")
    @ResponseStatus(HttpStatus.OK)
    fun getOrganization(@PathVariable invite: UUID): Mono<Organization> {
        return invites.service.getOrganizationByInvite(invite)
    }
}