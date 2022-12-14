package me.sknz.simpleblog.infra.config

import me.sknz.simpleblog.domain.model.BlogUser
import me.sknz.simpleblog.domain.model.Organization
import me.sknz.simpleblog.infra.repository.OrganizationRepository
import me.sknz.simpleblog.infra.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import java.time.OffsetDateTime
import java.util.*

@Component
class PopulateDatabase(
    val organizations: OrganizationRepository,
    val users: UserRepository,
    val encoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        organizations.findAll()
            .toMono()
            .switchIfEmpty {
                val organization = Organization()
                organization.id = UUID.randomUUID()
                organization.ownerId = UUID.randomUUID()
                organization.name = "MyBlog"
                organization.members.add(organization.ownerId)
                organization.createdAt = OffsetDateTime.now()
                organization.updatedAt = OffsetDateTime.now()
                organization.public = true
                organizations.save(organization)
            }.flatMap { org ->
                users.findAll()
                    .toMono()
                    .switchIfEmpty {
                        val user = BlogUser()
                        user.id = org.ownerId
                        user.name = "Cristian Ferreira"
                        user.email = "admin@admin.com"
                        user.username = "admin"
                        user.password = encoder.encode("12345678")
                        user.createdAt = OffsetDateTime.now()
                        user.updatedAt = OffsetDateTime.now()
                        user.organizations.add(org.id)

                        return@switchIfEmpty users.save(user)
                    }.zipWith(Mono.just(org))
            }.subscribe(::println)
    }

}