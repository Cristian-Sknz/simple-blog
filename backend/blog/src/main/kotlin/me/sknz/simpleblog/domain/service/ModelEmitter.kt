package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.domain.event.OrganizationDatabaseEvent
import me.sknz.simpleblog.domain.model.Model
import me.sknz.simpleblog.domain.utils.EventType
import me.sknz.simpleblog.infra.security.authentication.BearerTokenAuthentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder.getContext
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

abstract class ModelEmitter(
    val emitter: EmitterService
) {

    fun <T: Model<*>> Flux<T>.emit(organization: UUID, eventType: EventType = EventType.CREATED): Flux<T> {
        return this.flatMap { emit(organization, eventType, it) }
    }

    fun <T : Model<*>> Mono<T>.emit(organization: UUID, eventType: EventType = EventType.CREATED): Mono<T> {
        return this.flatMap { emit(organization, eventType, it) }
    }

    private fun <T : Model<*>> emit(organization: UUID, eventType: EventType = EventType.CREATED, model: T): Mono<T> {
        return getContext().map { it.authentication }
            .cast(BearerTokenAuthentication::class.java)
            .map {
                val event = OrganizationDatabaseEvent(
                    organization.toString(),
                    "${model.getTable()}_${eventType.type}",
                    model.toResponseModel(), it.clientId
                )

                emitter.emit(organization, event)
            }.thenReturn(model)
    }
}