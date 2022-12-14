package me.sknz.simpleblog.api.controller

import me.sknz.simpleblog.domain.service.InviteService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
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
}