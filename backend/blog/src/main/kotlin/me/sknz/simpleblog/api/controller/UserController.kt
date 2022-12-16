package me.sknz.simpleblog.api.controller

import me.sknz.simpleblog.domain.model.BlogUser
import me.sknz.simpleblog.domain.service.MemberService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class UserController(val members: MemberService) {

    @GetMapping(path = ["/api/me"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getSelfUser(): Mono<BlogUser> {
        return members.getSelfMember()
    }
}