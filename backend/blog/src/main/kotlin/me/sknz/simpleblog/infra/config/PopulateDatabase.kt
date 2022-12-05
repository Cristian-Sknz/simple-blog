package me.sknz.simpleblog.infra.config

import me.sknz.simpleblog.domain.model.BlogUser
import me.sknz.simpleblog.domain.model.Organization
import me.sknz.simpleblog.infra.repository.OrganizationRepository
import me.sknz.simpleblog.infra.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
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
            .switchIfEmpty {
                val organization = Organization()
                organization.id = UUID.randomUUID()
                organization.name = "MyBlog"
                organization.createdAt = OffsetDateTime.now()
                organization.updatedAt = OffsetDateTime.now()
                organizations.save(organization).subscribe()
            }.subscribe()

        users.findAll()
            .switchIfEmpty {
                val user = BlogUser()
                user.id = UUID.randomUUID()
                user.name = "Cristian Ferreira"
                user.email = "admin@admin.com"
                user.username = "admin"
                user.password = encoder.encode("12345678")
                user.createdAt = OffsetDateTime.now()
                user.updatedAt = OffsetDateTime.now()
                users.save(user).subscribe()
            }.zipWith(organizations.findAll()
                .take(1))
            .flatMap { tuple ->
                if (tuple.t1.organizations.isEmpty()) {
                    tuple.t2.members.add(tuple.t1.id)
                    organizations.save(tuple.t2).subscribe()
                    tuple.t1.organizations.add(tuple.t2.id)

                    return@flatMap users.save(tuple.t1)
                }
                return@flatMap Mono.just(tuple.t1)
            }.subscribe { println(it) }
    }

}