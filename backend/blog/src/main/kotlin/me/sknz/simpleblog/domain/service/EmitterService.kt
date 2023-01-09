package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.domain.event.OrganizationDatabaseEvent
import me.sknz.simpleblog.infra.repository.OrganizationRepository
import me.sknz.simpleblog.infra.security.authentication.BearerTokenAuthentication
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class EmitterService(private val organizations: OrganizationRepository) {

    private val map = ConcurrentHashMap<UUID, Many<OrganizationDatabaseEvent<*>>>()

    fun getOrganizationRealtimeChanges(organization: UUID): Flux<OrganizationDatabaseEvent<*>> {
        return getOrganizationSink(organization)
            // Filtro para evitar que quem enviou não receba a mudança
            .filterWhen { event ->
                ReactiveSecurityContextHolder.getContext()
                    .map { it.authentication }
                    .cast(BearerTokenAuthentication::class.java)
                    .map {
                        it.clientId == null || event.clientId == null || it.clientId != event.clientId
                    }
            }
    }

    private fun getOrganizationSink(organization: UUID): Flux<OrganizationDatabaseEvent<*>> {
        if (map.containsKey(organization)) {
            return map[organization]!!.asFlux()
        }

        return organizations.findById(organization)
            .switchIfEmpty { Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Organização não foi encontrada")) }
            .map {
                Sinks.many()
                    .multicast()
                    .directAllOrNothing<OrganizationDatabaseEvent<*>>()
            }.doOnNext { map[organization] = it }
            .flatMapMany { it.asFlux() }
    }

    fun emit(organization: UUID, event: OrganizationDatabaseEvent<*>) {
        if (!map.containsKey(organization)) {
            return
        }
        val sink = map[organization]!!
        if (sink.currentSubscriberCount() == 0) return
        sink.tryEmitNext(event)
    }
}