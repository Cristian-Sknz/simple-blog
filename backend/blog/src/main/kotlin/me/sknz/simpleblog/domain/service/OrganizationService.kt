package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.api.request.OrganizationDTO
import me.sknz.simpleblog.domain.model.BlogUser
import me.sknz.simpleblog.domain.model.Organization
import me.sknz.simpleblog.infra.repository.OrganizationRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import reactor.util.function.Tuple2
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Service
class OrganizationService(
    val members: MemberService,
    val organizations: OrganizationRepository
) {

    fun getOrganization(organization: UUID): Mono<Organization> {
        return getOrganizationWithMember(organization).map { it.t1 }
    }

    fun getOrganizationWithMember(organization: UUID): Mono<Tuple2<Organization, BlogUser>> {
        return members.isOrganizationMember(organization)
            .flatMap { organizations.findById(it.t2).zipWith(Mono.just(it.t1)) }
    }

    fun getOrganizationByInvite(invite: UUID): Mono<Organization> {
        return organizations.findByInvitesContains(invite)
            .switchIfEmpty {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhuma organização com este convite").toMono()
            }
    }

    fun getLoggedUserOrganizations(): Flux<Organization> {
        return members.getBlogUserDetails().flatMapMany {
            organizations.findByOwnerId(it.id)
        }
    }

    fun getOrganizations(): Flux<Organization> {
        return members.getSelfMember()
            .map { it.organizations.toFlux() }
            .flux().flatMap { organizations.findAllById(it) }
    }

    fun getPublicOrganizations(): Flux<Organization> {
        return organizations.findByPublic(true)
    }

    fun createOrganization(dto: OrganizationDTO): Mono<Organization> {
        return getLoggedUserOrganizations().collectList()
            .filter { it.size <= 5 }
            .switchIfEmpty {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Você só pode criar 5 organizações.").toMono()
            }.flatMap {
                members.getSelfMember()
            }.flatMap {
                val organization = Organization()
                val date = OffsetDateTime.now(ZoneOffset.UTC)
                organization.id = UUID.randomUUID()
                organization.ownerId = it.id
                organization.members.add(it.id)
                organization.name = dto.name
                organization.createdAt = date
                organization.updatedAt = date
                organization.public = dto.public ?: false
                it.organizations.add(organization.id)
                members.users.save(it).then(organizations.save(organization))
            }
    }
}