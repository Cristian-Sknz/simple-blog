package me.sknz.simpleblog.domain.service.sync

import me.sknz.simpleblog.api.request.SyncObjectRequest
import me.sknz.simpleblog.api.response.sync.*
import me.sknz.simpleblog.domain.model.*
import me.sknz.simpleblog.domain.service.EmitterService
import me.sknz.simpleblog.domain.service.MemberService
import me.sknz.simpleblog.domain.service.ModelEmitter
import me.sknz.simpleblog.domain.utils.EventType
import me.sknz.simpleblog.domain.utils.SyncObject
import me.sknz.simpleblog.domain.utils.getSyncObjectFrom
import me.sknz.simpleblog.infra.repository.*
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import reactor.util.function.Tuple2
import java.time.OffsetDateTime
import java.util.*

@Service
class SyncService(
    val members: MemberService,
    emitter: EmitterService,
    val posts: PostRepository,
    val comments: PostCommentRepository,
    val likes: PostLikeRepository,
    val commentLikes: CommentLikeRepository,
    val deletes: DeletedObjectRepository
): ModelEmitter(emitter) {

    fun pushChanges(organization: UUID, sync: SyncObjectRequest): Mono<Void> {
        return members.isOrganizationMember(organization)
            .flatMap { it.t1.id.toMono().zipWith(sync.toMono()) }
            .flatMapMany { tuple ->
                Flux.create { sink ->
                    sink.processPosts(organization, tuple)
                    sink.complete()
                }.flatMap { it }
            }.then()
    }

    fun FluxSink<Flux<Void>>.processPosts(organization: UUID, tuple: Tuple2<UUID, SyncObjectRequest>) {
        val post = PostModelSync(posts, organization, tuple.t1, tuple.t2.changes.posts)
        next(Flux.empty<Any>()
            .thenMany(posts.saveAll(post.onProcessCreated().emit(organization, EventType.CREATED)))
            .thenMany(posts.saveAll(post.onProcessUpdated().emit(organization, EventType.UPDATED)))
            .thenMany(posts.deleteAll(post.onProcessDeleted().emit(organization, EventType.DELETED)))
        )
    }

    fun pullChanges(organization: UUID, timestamp: OffsetDateTime? = null): Mono<Map<String, Any>> {
        return members.isOrganizationMember(organization).map { it.t2 }
            .flatMapMany { uuid ->
                Flux.create { sink ->
                    val deletions = getDeletions(uuid, timestamp).cache()
                    val posts = posts.findByOrganizationId(organization).cache()
                    sink.apply {
                        next(getMemberSync(uuid, timestamp, deletions).flux())
                        next(getPostSync(uuid, posts, timestamp, deletions).flux())
                        next(getCommentsSync(uuid, posts, timestamp, deletions).flux())
                        next(getPostLikeSync(uuid, posts, timestamp, deletions).flux())
                        next(getCommentLikeSync(uuid, posts, timestamp, deletions).flux())
                    }.complete()
                }.flatMap { it }
            }.collectMap({ it.table }, { it })
    }

    fun getMemberSync(organization: UUID, timestamp: OffsetDateTime?, deletions: Flux<DeletedObject>): Mono<SyncObject> {
        if (timestamp == null) {
            return getSyncObjectFrom(BlogUser.table, members.users.findByOrganizationsContains(organization)
                .map(::SyncedUserResponse), null, deletions)
        }

        return getSyncObjectFrom(BlogUser.table,
            members.users.findByOrganizationsContainsAndCreatedAtAfter(organization, timestamp).map(::SyncedUserResponse),
            members.users.findByOrganizationsContainsAndUpdatedAtAfter(organization, timestamp).map(::SyncedUserResponse),
            deletions
        )
    }

    fun getPostSync(organization: UUID, posts: Flux<BlogPost>, timestamp: OffsetDateTime?, deletions: Flux<DeletedObject>): Mono<SyncObject> {
        if (timestamp == null) {
            return getSyncObjectFrom(BlogPost.table, posts.map(::SyncedPostResponse), null, deletions)
        }

        return getSyncObjectFrom(
            BlogPost.table,
            this.posts.findByOrganizationIdAndCreatedAtAfter(organization, timestamp).map(::SyncedPostResponse),
            this.posts.findByOrganizationIdAndUpdatedAtAfter(organization, timestamp).map(::SyncedPostResponse),
            deletions
        )
    }

    fun getCommentsSync(organization: UUID, posts: Flux<BlogPost>, timestamp: OffsetDateTime?, deletions: Flux<DeletedObject>): Mono<SyncObject> {
        val ids = posts.flatMap { Flux.fromIterable(it.comments) }
        if (timestamp == null) {
            return getSyncObjectFrom(PostComment.table, comments.findAllById(ids)
                .map(::SyncedCommentResponse), null, deletions)
        }

        return getSyncObjectFrom(PostComment.table,
            comments.findAllByIdAndCreatedAtAfter(ids, timestamp).map(::SyncedCommentResponse),
            comments.findAllByIdAndUpdatedAtAfter(ids, timestamp).map(::SyncedCommentResponse),
            deletions
        )
    }

    fun getPostLikeSync(organization: UUID, posts: Flux<BlogPost>, timestamp: OffsetDateTime?, deletions: Flux<DeletedObject>): Mono<SyncObject> {
        val ids = posts.flatMap { Flux.fromIterable(it.likes) }
        if (timestamp == null) {
            return getSyncObjectFrom(PostLike.table, likes.findAllById(ids).map(::SyncedLikeResponse), null, deletions)
        }

        return getSyncObjectFrom(PostLike.table,
            likes.findAllByIdAndCreatedAtAfter(ids, timestamp).map(::SyncedLikeResponse),
            likes.findAllByIdAndUpdatedAtAfter(ids, timestamp).map(::SyncedLikeResponse),
            deletions
        )
    }

    fun getCommentLikeSync(organization: UUID, posts: Flux<BlogPost>, timestamp: OffsetDateTime?, deletions: Flux<DeletedObject>): Mono<SyncObject> {
        val ids = posts.flatMap { Flux.fromIterable(it.comments) }
        if (timestamp == null) {
            return getSyncObjectFrom(CommentLike.table, commentLikes.findAllByCommentIdIn(ids)
                .map(::SyncedCommentLikeResponse), null, deletions)
        }

        return getSyncObjectFrom(CommentLike.table,
            commentLikes.findAllByCommentIdInAndCreatedAtAfter(ids, timestamp).map(::SyncedCommentLikeResponse),
            commentLikes.findAllByCommentIdInAndUpdatedAtAfter(ids, timestamp).map(::SyncedCommentLikeResponse),
            deletions
        )
    }

    fun getDeletions(organization: UUID, timestamp: OffsetDateTime?): Flux<DeletedObject> {
        if (timestamp == null) return Flux.empty()
        return deletes.findByOrganizationIdAndDeletedAtAfter(organization, timestamp)
    }
}