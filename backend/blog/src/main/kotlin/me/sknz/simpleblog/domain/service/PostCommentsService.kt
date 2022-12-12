package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.domain.dto.CommentDTO
import me.sknz.simpleblog.domain.model.BlogPost
import me.sknz.simpleblog.domain.model.CommentLike
import me.sknz.simpleblog.domain.model.PostComment
import me.sknz.simpleblog.domain.utils.EventType
import me.sknz.simpleblog.infra.repository.CommentLikeRepository
import me.sknz.simpleblog.infra.repository.PostCommentRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Service
class PostCommentsService(
    val member: MemberService,
    val posts: PostService,
    val comments: PostCommentRepository,
    val likes: CommentLikeRepository,
): ModelEmitter(posts.emitter) {

    fun getPostComments(organization: UUID, post: UUID): Flux<PostComment> {
        return getPost(organization, post).flatMapMany { comments.findByPostId(it.id) }
    }

    fun getPostCommentById(organization: UUID, postId: UUID, comment: UUID): Mono<PostComment> {
        return getPost(organization, postId).flatMap { post ->
            comments.findByPostIdAndId(post.id, comment).filter { comment ->
                post.comments.any { comment.id == it }
            }
        }.switchIfEmpty {
            Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum comentário com este id"))
        }
    }

    fun createPostComment(organization: UUID, postId: UUID, comment: CommentDTO): Mono<PostComment> {
        return getPost(organization, postId).zipWith(member.getBlogUserDetails())
            .flatMap { tuple ->
                comments.save(comment.toPostComment(tuple.t1.id, tuple.t2.id))
                    .zipWith(Mono.just(tuple.t1))
            }.flatMap { tuple ->
                tuple.t2.comments.add(tuple.t1.id)
                posts.posts.save(tuple.t2).then(Mono.just(tuple.t1))
            }.emit(organization)
    }

    fun likeTheComment(organization: UUID, postId: UUID, commentId: UUID): Mono<CommentLike> {
        return getPostCommentById(organization, postId, commentId)
            .zipWith(member.getBlogUserDetails())
            .filterWhen { tuple -> likes.existsByCommentIdAndUserId(tuple.t1.id, tuple.t2.id).map { !it } }
            .switchIfEmpty {
                Mono.error(ResponseStatusException(HttpStatus.CONFLICT, "Você já curtiu este comentário"))
            }.flatMap { tuple ->
                val like = CommentLike()
                like.id = UUID.randomUUID()
                like.commentId = tuple.t1.id
                like.userId = tuple.t2.id
                like.createdAt = OffsetDateTime.now(ZoneOffset.UTC)
                like.updatedAt = OffsetDateTime.now(ZoneOffset.UTC)
                tuple.t1.likes.add(like.id)
                comments.save(tuple.t1).then(likes.save(like))
            }.emit(organization)
    }

    fun dislikeTheComment(organization: UUID, postId: UUID, commentId: UUID): Mono<Void> {
        return getPostCommentById(organization, postId, commentId)
            .zipWith(member.getBlogUserDetails())
            .flatMap { tuple ->
                likes.findByCommentIdAndUserId(tuple.t1.id, tuple.t2.id).zipWith(Mono.just(tuple.t1))
            }.switchIfEmpty {
                Mono.error(ResponseStatusException(HttpStatus.CONFLICT, "Você não tem curtidas nesse comentário"))
            }.flatMap { tuple ->
                tuple.t2.likes.removeIf { it == tuple.t1.id }
                likes.delete(tuple.t1).then(comments.save(tuple.t2)).then(Mono.just(tuple.t1))
            }.emit(organization, EventType.DELETED).then()
    }

    private fun getPost(organization: UUID, postId: UUID): Mono<BlogPost> {
        return posts.getPost(organization, postId).switchIfEmpty {
            Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado nenhum post com este id"))
        }
    }
}
