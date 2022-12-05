package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.domain.event.OrganizationDatabaseEvent
import me.sknz.simpleblog.infra.repository.OrganizationRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class EmitterService(private val organizations: OrganizationRepository) {

    val map = ConcurrentHashMap<UUID, Many<OrganizationDatabaseEvent<*>>>()

    fun getOrganizationRealtimeChanges(organization: UUID): Flux<OrganizationDatabaseEvent<*>> {
        return getOrganizationSink(organization).flux().flatMap { it.asFlux() }
    }

    private fun getOrganizationSink(organization: UUID): Mono<Many<OrganizationDatabaseEvent<*>>> {
        if (map.containsKey(organization)) {
            return Mono.just(map[organization]!!)
        }
        val sink = organizations.findById(organization)
            .switchIfEmpty { Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Organização não foi encontrada")) }
            .map {
                Sinks.many()
                    .multicast()
                    .onBackpressureBuffer<OrganizationDatabaseEvent<*>>()
            }.cache()
        sink.subscribe { map[organization] = it }
        return sink
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