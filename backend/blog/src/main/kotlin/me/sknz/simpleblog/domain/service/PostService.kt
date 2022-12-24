package me.sknz.simpleblog.domain.service

import me.sknz.simpleblog.api.request.PostDTO
import me.sknz.simpleblog.domain.model.BlogPost
import me.sknz.simpleblog.domain.model.BlogUser
import me.sknz.simpleblog.domain.model.PostLike
import me.sknz.simpleblog.domain.utils.EventType
import me.sknz.simpleblog.infra.repository.PostLikeRepository
import me.sknz.simpleblog.infra.repository.PostRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import reactor.util.function.Tuple2
import java.util.*

@Service
class PostService(
    val organizations: OrganizationService,
    val posts: PostRepository,
    val likes: PostLikeRepository,
    emitter: EmitterService
): ModelEmitter(emitter) {

    fun getPosts(organization: UUID): Flux<BlogPost> {
        return organizations.members.isOrganizationMember(organization)
            .flatMapMany { posts.findByOrganizationId(it.t2) }
    }

    fun getPost(organization: UUID, post: UUID): Mono<BlogPost> {
        return getPostWithUser(organization, post).map { it.t1 }
    }

    fun getPostWithUser(organization: UUID, post: UUID): Mono<Tuple2<BlogPost, BlogUser>> {
        return organizations.members.isOrganizationMember(organization)
            .flatMap { posts.findByOrganizationIdAndId(it.t2, post)
                .switchIfEmpty {
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Post solicitado não foi encontrado").toMono()
                }.zipWith(it.t1.toMono())
            }
    }

    fun create(organization: UUID, postDTO: PostDTO): Mono<BlogPost> {
        return organizations.members.isOrganizationMember(organization)
            .flatMap {
                val post = postDTO.toNewBlogPost()
                post.userId = it.t1.id
                post.organizationId = it.t2
                posts.save(post)
            }.emit(organization)
    }

    fun delete(organization: UUID, postId: UUID): Mono<Void> {
        return getPostWithUser(organization, postId)
            .filter { it.t1.userId == it.t2.id }
            .map { it.t1 }
            .flatMap { posts.delete(it).thenReturn(it) }
            .emit(organization, EventType.DELETED)
            .switchIfEmpty {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não tem permissão para deletar um post que não é seu!").toMono()
            }.then()
    }

    fun likeThePost(organization: UUID, postId: UUID): Mono<PostLike> {
        return getPostWithUser(organization, postId).zipWhen { tuple ->
            likes.findByPostIdAndUserId(tuple.t1.id, tuple.t2.id).flatMap {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já deu like neste post!")
                    .toMono<PostLike>()
            }.switchIfEmpty { PostLike.from(tuple.t1.id, tuple.t2.id).toMono() }
        }.flatMap { tuple ->
            val post = tuple.t1.t1
            post.likes.add(tuple.t2.id)
            posts.save(post).then(likes.save(tuple.t2))
        }.emit(organization, EventType.CREATED)
    }

    fun dislikeThePost(organization: UUID, postId: UUID): Mono<Void> {
        return getPostWithUser(organization, postId).zipWhen { tuple ->
            likes.findByPostIdAndUserId(tuple.t1.id, tuple.t2.id).switchIfEmpty {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não deu like neste post!")
                    .toMono()
            }
        }.flatMap { tuple ->
            val post = tuple.t1.t1
            post.likes.remove(tuple.t2.id)
            posts.save(post).then(likes.delete(tuple.t2)).thenReturn(tuple.t2)
        }.emit(organization, EventType.DELETED).then()
    }
}