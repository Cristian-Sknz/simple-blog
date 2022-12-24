package me.sknz.simpleblog.api.controller

import jakarta.validation.Valid
import me.sknz.simpleblog.api.request.CommentDTO
import me.sknz.simpleblog.domain.model.CommentLike
import me.sknz.simpleblog.domain.model.PostComment
import me.sknz.simpleblog.domain.service.PostCommentsService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/api/organizations/{organization}/posts/{post}/comments")
class PostCommentsController(
    private val comments: PostCommentsService
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPostComments(@PathVariable organization: UUID,
                        @PathVariable post: UUID): Flux<PostComment> {
        return comments.getPostComments(organization, post)
    }

    @GetMapping(path = ["/{comment}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getPostComment(@PathVariable organization: UUID,
                       @PathVariable post: UUID,
                       @PathVariable comment: UUID) : Mono<PostComment> {
        return comments.getPostCommentById(organization, post, comment)
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createPostComment(@PathVariable organization: UUID,
                          @PathVariable post: UUID,
                          @RequestBody @Valid comment: CommentDTO
    ): Mono<PostComment> {
        return comments.createPostComment(organization, post, comment)
    }

    @PostMapping(path = ["/{comment}/likes"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun likeTheComment(@PathVariable organization: UUID,
                       @PathVariable post: UUID,
                       @PathVariable comment: UUID) : Mono<CommentLike> {
        return comments.likeTheComment(organization, post, comment)
    }

    @DeleteMapping(path = ["/{comment}/likes"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun dislikeTheComment(@PathVariable organization: UUID,
                          @PathVariable post: UUID,
                          @PathVariable comment: UUID) : Mono<Void> {
        return comments.dislikeTheComment(organization, post, comment)
    }
}