package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.domain.dto.OrganizationInviteDTO
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import java.util.*

@Service
class InviteService(val service: OrganizationService) {

    fun joinOrganization(invite: UUID): Mono<Void> {
        return service.getOrganizationByInvite(invite).zipWhen {
            service.members.getSelfMember()
        }.filter { tuple ->
            tuple.t1.members.contains(tuple.t2.id).not()
        }.switchIfEmpty {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Você já faz parte desta organização!").toMono()
        }.flatMap {
            it.t1.members.add(it.t2.id)
            it.t2.organizations.add(it.t1.id)
            service.organizations.save(it.t1).then(service.members.users.save(it.t2))
        }.then()
    }

    fun joinPublicOrganization(organization: UUID): Mono<Void> {
        return service.organizations.findById(organization)
            .filter { it.public }
            .switchIfEmpty {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta organização não é publica ou não existe!").toMono()
            }.zipWhen {
                service.members.getSelfMember()
            }.flatMap {
                it.t1.members.add(it.t2.id)
                it.t2.organizations.add(it.t1.id)
                service.organizations.save(it.t1)
                    .then(service.members.users.save(it.t2))
            }.then()
    }

    fun leaveOrganization(organization: UUID): Mono<Void> {
        return service.getOrganizationWithMember(organization)
            .filter { it.t1.ownerId != it.t2.id }
            .switchIfEmpty {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Você não pode sair de uma organização que é dono!").toMono()
            }
            .flatMap {
                it.t1.members.remove(it.t2.id)
                it.t2.organizations.remove(it.t1.id)
                service.organizations.save(it.t1).then(service.members.users.save(it.t2))
            }.then()
    }

    fun createInvite(organization: UUID): Mono<OrganizationInviteDTO> {
        return service.members.isOrganizationMember(organization)
            .flatMap { service.organizations.findById(it.t2).zipWith(Mono.just(it.t1)) }
            .filter { it.t1.ownerId == it.t2.id }
            .switchIfEmpty {
                ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para criar convites").toMono()
            }.flatMap {
                val dto = OrganizationInviteDTO(UUID.randomUUID(), it.t1.id)
                it.t1.invites.add(dto.invite)
                service.organizations.save(it.t1).thenReturn(dto)
            }
    }

    fun removeInvite(organization: UUID, invite: UUID): Mono<Void> {
        return service.members.isOrganizationMember(organization)
            .flatMap { service.organizations.findById(it.t2).zipWith(Mono.just(it.t1)) }
            .filter { it.t1.ownerId == it.t2.id }
            .switchIfEmpty {
                ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para criar convites").toMono()
            }.filter { tuple -> tuple.t1.invites.any { it === invite } }
            .switchIfEmpty {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado o convite nesta organização").toMono()
            }.flatMap {
                it.t1.invites.remove(invite)
                service.organizations.save(it.t1)
            }.then()
    }
}