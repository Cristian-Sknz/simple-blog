package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.domain.model.BlogUser
import me.sknz.simpleblog.infra.repository.OrganizationRepository
import me.sknz.simpleblog.infra.repository.UserRepository
import me.sknz.simpleblog.infra.security.userdetails.BlogUserDetails
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import reactor.util.function.Tuple2
import java.util.UUID

@Service
class MemberService(
    val users: UserRepository,
    private val organizations: OrganizationRepository
) {

    fun getMembers(organization: UUID): Flux<BlogUser> {
        return isOrganizationMember(organization)
            .flatMap { organizations.findById(it.t2)  }
            .map { users.findAllById(it.members) }
            .flux().flatMap { it }
    }

    fun getMember(organization: UUID, member: UUID): Mono<BlogUser> {
        return isOrganizationMember(organization).flux()
            .flatMap { organizations.findById(it.t2)  }
            .flatMap { it.members.toFlux() }
            .filter { it == member }
            .toMono().flatMap { users.findById(it) }
            .switchIfEmpty {
                Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum membro com este ID"))
            }
    }

    fun isOrganizationMember(organization: UUID): Mono<Tuple2<BlogUser, UUID>> {
        return getSelfMember()
            .zipWith(Mono.just(organization))
            .filter { tuple -> tuple.t1.organizations.any { it == tuple.t2 } }
            .switchIfEmpty {
                Mono.error(ResponseStatusException(HttpStatus.FORBIDDEN, "Você não faz parte desta organização"))
            }
    }

    fun getSelfMember(): Mono<BlogUser> {
        return getBlogUserDetails()
            .flatMap { users.findById(it.id) }
            .switchIfEmpty {
                Mono.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro ao tentar obter o usuário logado"))
            }
    }

    fun getBlogUserDetails(): Mono<BlogUserDetails> {
        return ReactiveSecurityContextHolder.getContext()
            .map { it.authentication.principal as BlogUserDetails }
    }
}