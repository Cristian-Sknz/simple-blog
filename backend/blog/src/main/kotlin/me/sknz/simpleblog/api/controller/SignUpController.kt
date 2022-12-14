package me.sknz.simpleblog.api.controller

import jakarta.validation.Valid
import me.sknz.simpleblog.domain.dto.SignUpDTO
import me.sknz.simpleblog.domain.service.SignUpService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/auth")
class SignUpController(
    val service: SignUpService
) {
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody dto: SignUpDTO): Mono<SignUpDTO> {
        return service.createUser(dto)
    }
}