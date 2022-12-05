package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.domain.model.PostComment
import me.sknz.simpleblog.domain.utils.SyncObject
import me.sknz.simpleblog.domain.utils.getSyncObjectFrom
import me.sknz.simpleblog.infra.repository.PostCommentRepository
import me.sknz.simpleblog.infra.repository.PostRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.*

@Service
class SyncService(
    val members: MemberService,
    val posts: PostRepository,
    val comments: PostCommentRepository
) {

    fun pullChanges(organization: UUID, timestamp: OffsetDateTime? = null): Mono<Map<String, Any>> {
        return members.isOrganizationMember(organization).map { it.t2 }
            .flatMapMany { uuid ->
                Flux.create { sink ->
                    sink.next(getPostSync(uuid, timestamp).flux())
                        .next(getMemberSync(uuid, timestamp).flux())
                        .next(getCommentsSync(uuid, timestamp).flux())
                        .complete()
                }.flatMap { it }
            }.collectMap({ it.table }, { it })
    }

    fun getMemberSync(organization: UUID, timestamp: OffsetDateTime?): Mono<SyncObject> {
        if (timestamp == null) {
            return getSyncObjectFrom("users", members.users.findByOrganizationsContains(organization), null)
        }

        return getSyncObjectFrom("users",
            members.users.findByOrganizationsContainsAndCreatedAtAfter(organization, timestamp),
            members.users.findByOrganizationsContainsAndUpdatedAtAfter(organization, timestamp)
        )
    }

    fun getPostSync(organization: UUID, timestamp: OffsetDateTime?): Mono<SyncObject> {
        if (timestamp == null) {
            return getSyncObjectFrom("posts", posts.findByOrganizationId(organization), null)
        }

        return getSyncObjectFrom("posts",
            posts.findByOrganizationIdAndCreatedAtAfter(organization, timestamp),
            posts.findByOrganizationIdAndUpdatedAtAfter(organization, timestamp)
        )
    }

    fun getCommentsSync(organization: UUID, timestamp: OffsetDateTime?): Mono<SyncObject> {
        val ids = posts.findByOrganizationId(organization).flatMap { Flux.fromIterable(it.comments) }
        if (timestamp == null) {
            return getSyncObjectFrom(PostComment.table, comments.findAllById(ids), null)
        }

        return getSyncObjectFrom(PostComment.table,
            comments.findAllByIdAndCreatedAtAfter(ids, timestamp),
            null //comments.findByPostIdAndUpdatedAtAfter(ids, timestamp)
        )
    }
}