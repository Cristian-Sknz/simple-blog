package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.DeletedObject
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import java.time.OffsetDateTime
import java.util.UUID

interface DeletedObjectRepository : ReactiveMongoRepository<DeletedObject, UUID> {

    fun findByOrganizationIdAndDeletedAtAfter(organization: UUID, deletedAt: OffsetDateTime): Flux<DeletedObject>

}