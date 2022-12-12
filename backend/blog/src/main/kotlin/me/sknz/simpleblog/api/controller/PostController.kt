package me.sknz.simpleblog.api.controller

import me.sknz.simpleblog.domain.dto.PostDTO
import me.sknz.simpleblog.domain.model.BlogPost
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
    fun getOrganizationPosts(@PathVariable organization: UUID): Flux<BlogPost> {
        return posts.getPosts(organization)
    }

    @GetMapping(path = ["/{post}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getOrganizationPost(@PathVariable organization: UUID,
                            @PathVariable post: UUID): Mono<BlogPost> {
        return posts.getPost(organization, post)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewPost(@PathVariable organization: UUID,
                      @RequestBody post: PostDTO): Mono<BlogPost> {
        return posts.create(organization, post)
    }
}