package me.sknz.simpleblog.api.controller

import jakarta.validation.Valid
import me.sknz.simpleblog.api.response.sync.SyncedPostResponse
import me.sknz.simpleblog.api.request.PostDTO
import me.sknz.simpleblog.domain.model.PostLike
import me.sknz.simpleblog.domain.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/api/organizations/{organization}/posts")
class PostController(
    private val posts: PostService
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getOrganizationPosts(@PathVariable organization: UUID): Flux<SyncedPostResponse> {
        return posts.getPosts(organization).map(::SyncedPostResponse)
    }

    @GetMapping(path = ["/{post}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getOrganizationPost(@PathVariable organization: UUID,
                            @PathVariable post: UUID): Mono<SyncedPostResponse> {
        return posts.getPost(organization, post).map(::SyncedPostResponse)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewPost(@PathVariable organization: UUID,
                      @RequestBody @Valid post: PostDTO
    ): Mono<SyncedPostResponse> {
        return posts.create(organization, post).map(::SyncedPostResponse)
    }

    @DeleteMapping(path = ["/{post}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMyPost(@PathVariable organization: UUID,
                     @PathVariable post: UUID): Mono<Void> {
        TODO("Desativado por enquanto...") //return posts.delete(organization, post)
    }

    @PostMapping(path = ["/{post}/likes"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun likeThePost(@PathVariable organization: UUID,
                       @PathVariable post: UUID) : Mono<PostLike> {
        return posts.likeThePost(organization, post)
    }

    @DeleteMapping(path = ["/{post}/likes"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun dislikeThePost(@PathVariable organization: UUID,
                          @PathVariable post: UUID) : Mono<Void> {
        return posts.dislikeThePost(organization, post)
    }
}