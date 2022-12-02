package me.sknz.simpleblog.infra.config

import me.sknz.simpleblog.domain.model.BlogUser
import me.sknz.simpleblog.domain.model.Organization
import me.sknz.simpleblog.infra.repository.OrganizationRepository
import me.sknz.simpleblog.infra.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
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
                user.email = "admin@admin.com"
                user.username = "admin"
                user.password = encoder.encode("12345678")
                user.createdAt = OffsetDateTime.now()
                user.updatedAt = OffsetDateTime.now()
                users.save(user).subscribe()
            }.subscribe()
    }

}