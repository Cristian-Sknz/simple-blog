package me.sknz.simpleblog.infra.repository

import me.sknz.simpleblog.domain.model.Organization
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.UUID

interface OrganizationRepository: ReactiveMongoRepository<Organization, UUID> {
}