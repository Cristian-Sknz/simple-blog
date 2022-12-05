package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.domain.model.Organization
import me.sknz.simpleblog.infra.repository.OrganizationRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.util.*

@Service
class OrganizationService(
    private val members: MemberService,
    private val organizations: OrganizationRepository
) {

    fun getOrganization(organization: UUID): Mono<Organization> {
        return members.isOrganizationMember(organization)
            .flatMap { organizations.findById(it.t2) }
    }

    fun getOrganizations(): Flux<Organization> {
        return members.getSelfMember()
            .map { it.organizations.toFlux() }
            .flux().flatMap { organizations.findAllById(it) }
    }
}