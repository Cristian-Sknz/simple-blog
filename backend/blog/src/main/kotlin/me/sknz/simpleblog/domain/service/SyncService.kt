package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.domain.model.BlogPost
import me.sknz.simpleblog.domain.model.BlogUser
import me.sknz.simpleblog.domain.model.DeletedObject
import me.sknz.simpleblog.domain.model.PostComment
import me.sknz.simpleblog.domain.utils.SyncObject
import me.sknz.simpleblog.domain.utils.getSyncObjectFrom
import me.sknz.simpleblog.infra.repository.DeletedObjectRepository
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
    val comments: PostCommentRepository,
    val deletes: DeletedObjectRepository
) {

    fun pullChanges(organization: UUID, timestamp: OffsetDateTime? = null): Mono<Map<String, Any>> {
        return members.isOrganizationMember(organization).map { it.t2 }
            .flatMapMany { uuid ->
                Flux.create { sink ->
                    val deletions = getDeletions(uuid, timestamp).cache()

                    sink.next(getPostSync(uuid, timestamp, deletions).flux())
                        .next(getMemberSync(uuid, timestamp, deletions).flux())
                        .next(getCommentsSync(uuid, timestamp, deletions).flux())
                        .complete()
                }.flatMap { it }
            }.collectMap({ it.table }, { it })
    }

    fun getMemberSync(organization: UUID, timestamp: OffsetDateTime?, deletions: Flux<DeletedObject>): Mono<SyncObject> {
        if (timestamp == null) {
            return getSyncObjectFrom(BlogUser.table, members.users.findByOrganizationsContains(organization), null, deletions)
        }

        return getSyncObjectFrom(BlogUser.table,
            members.users.findByOrganizationsContainsAndCreatedAtAfter(organization, timestamp),
            members.users.findByOrganizationsContainsAndUpdatedAtAfter(organization, timestamp),
            deletions
        )
    }

    fun getPostSync(organization: UUID, timestamp: OffsetDateTime?, deletions: Flux<DeletedObject>): Mono<SyncObject> {
        if (timestamp == null) {
            return getSyncObjectFrom(BlogPost.table, posts.findByOrganizationId(organization), null, deletions)
        }

        return getSyncObjectFrom(
            BlogPost.table,
            posts.findByOrganizationIdAndCreatedAtAfter(organization, timestamp),
            posts.findByOrganizationIdAndUpdatedAtAfter(organization, timestamp),
            deletions
        )
    }

    fun getCommentsSync(organization: UUID, timestamp: OffsetDateTime?, deletions: Flux<DeletedObject>): Mono<SyncObject> {
        val ids = posts.findByOrganizationId(organization).flatMap { Flux.fromIterable(it.comments) }
        if (timestamp == null) {
            return getSyncObjectFrom(PostComment.table, comments.findAllById(ids), null, deletions)
        }

        return getSyncObjectFrom(PostComment.table,
            comments.findAllByIdAndCreatedAtAfter(ids, timestamp),
            comments.findAllByIdAndUpdatedAtAfter(ids, timestamp),
            deletions
        )
    }

    fun getDeletions(organization: UUID, timestamp: OffsetDateTime?): Flux<DeletedObject> {
        if (timestamp == null) return Flux.empty()
        return deletes.findByOrganizationIdAndDeletedAtAfter(organization, timestamp)
    }
}