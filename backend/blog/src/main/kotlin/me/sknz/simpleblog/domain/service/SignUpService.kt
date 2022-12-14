package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.domain.dto.SignUpDTO
import me.sknz.simpleblog.domain.model.BlogUser
import me.sknz.simpleblog.infra.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Service
class SignUpService(
    val users: UserRepository,
    val encoder: PasswordEncoder
) {

    fun createUser(dto: SignUpDTO): Mono<SignUpDTO> {
        return users.existsByEmail(dto.email)
            .zipWith(users.existsByUsername(dto.username))
            .flatMap { tuple ->
                when {
                    tuple.t1 -> ResponseStatusException(HttpStatus.BAD_REQUEST, "Email").toMono()
                    tuple.t2 -> ResponseStatusException(HttpStatus.BAD_REQUEST, "Username").toMono()
                    else -> {
                        val user = BlogUser()
                        val date = OffsetDateTime.now(ZoneOffset.UTC)

                        user.id = UUID.randomUUID()
                        user.name = dto.name
                        user.username = dto.username
                        user.email = dto.email
                        user.password = encoder.encode(dto.password)
                        user.createdAt = date
                        user.updatedAt = date
                        users.save(user).thenReturn(dto)
                    }
                }
            }
    }

}