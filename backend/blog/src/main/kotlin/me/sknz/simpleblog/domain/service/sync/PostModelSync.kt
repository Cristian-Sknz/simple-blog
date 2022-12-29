package me.sknz.simpleblog.domain.service.sync

import me.sknz.simpleblog.api.request.SyncObjectRequest
import me.sknz.simpleblog.api.request.sync.SyncedPostRequest
import me.sknz.simpleblog.domain.model.BlogPost
import me.sknz.simpleblog.infra.repository.PostRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

open class PostModelSync(
    private val posts: PostRepository,
    private val organization: UUID,
    private val user: UUID,
    objects: SyncObjectRequest.ChangeObject<SyncedPostRequest>,
) : AbstractModelSync<SyncedPostRequest, BlogPost>(objects) {

    constructor(posts: PostRepository,
                organization: UUID,
                user: UUID, objects: SyncObjectRequest): this(posts, organization, user, objects.changes.posts)

    override fun getExists(ids: Flux<UUID>): Flux<BlogPost> {
        return posts.findAllById(ids).filter {
            it.organizationId == organization
        }
    }

    override fun onBeforeProcessFilter(value: BlogPost): Mono<Boolean> {
        return Mono.just(value.userId == user)
    }

    override fun onMergeWithExists(change: BlogPost, original: BlogPost): BlogPost {
        return original.merge(change)
    }

    override fun onConvertToDatabaseModel(item: SyncedPostRequest): BlogPost {
        return item.toBlogPost(organization)
    }
}