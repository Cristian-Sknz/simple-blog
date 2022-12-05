package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.domain.dto.PostDTO
import me.sknz.simpleblog.domain.event.OrganizationDatabaseEvent
import me.sknz.simpleblog.domain.model.BlogPost
import me.sknz.simpleblog.infra.repository.OrganizationRepository
import me.sknz.simpleblog.infra.repository.PostRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.*

@Service
class PostService(
    val member: MemberService,
    val posts: PostRepository,
    val organizations: OrganizationRepository,
    val emitter: EmitterService
) {

    fun getPosts(organization: UUID): Flux<BlogPost> {
        return member.isOrganizationMember(organization)
            .flux().flatMap { posts.findByOrganizationId(it.t2) }
    }

    fun getPost(organization: UUID, id: UUID): Mono<BlogPost> {
        return member.isOrganizationMember(organization)
            .map { it.t2 }
            .zipWith(Mono.just(id))
            .flatMap { posts.findByOrganizationIdAndId(it.t1, it.t2) }
    }

    fun create(organization: UUID, postDTO: PostDTO): Mono<BlogPost> {
        return member.isOrganizationMember(organization)
            .flatMap {
                val post = postDTO.toNewBlogPost()
                post.userId = it.t1.id
                post.organizationId = it.t2
                posts.save(post).zipWith(organizations.findById(it.t2))
            }.map {
                emitter.emit(organization, OrganizationDatabaseEvent(
                    organization = it.t2.name,
                    type = "posts_create",
                    payload = it.t1
                ))
                return@map it.t1
            }
    }
}