package me.sknz.simpleblog.api.controller

import me.sknz.simpleblog.domain.model.BlogUser
import me.sknz.simpleblog.domain.service.MemberService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
@RequestMapping("/api/organizations/{organization}/members")
class MemberController(
    val members: MemberService,
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMembers(@PathVariable organization: UUID): Flux<BlogUser> {
        return members.getMembers(organization)
    }

    @GetMapping(path = ["/{member}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMember(@PathVariable organization: UUID,
                  @PathVariable member: UUID): Mono<BlogUser> {
        return members.getMember(organization, member)
    }
}