package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.Organization
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface OrganizationRepository: ReactiveMongoRepository<Organization, UUID> {

    fun findByInvitesContains(invite: UUID): Mono<Organization>
    fun findByOwnerId(ownerId: UUID): Flux<Organization>
    fun findByPublic(public: Boolean): Flux<Organization>

}