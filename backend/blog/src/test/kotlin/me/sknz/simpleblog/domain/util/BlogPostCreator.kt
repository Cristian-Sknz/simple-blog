package me.sknz.simpleblog.domain.util

import me.sknz.simpleblog.domain.model.BlogPost
import java.time.OffsetDateTime
import java.util.*

object BlogPostCreator {

    fun createRandomPost(): BlogPost {
        return BlogPost().let {
            val value = Random().nextInt(200)
            it.id = UUID.randomUUID()
            it.content = "Conteudo $value"
            it.title = "Titulo nº $value"
            it.userId = UUID.randomUUID()
            it.organizationId = UUID.randomUUID()
            it.createdAt = OffsetDateTime.now()
            it.updatedAt = OffsetDateTime.now()
            it
        }
    }

    fun createRandomWithId(uuid: UUID, organization: UUID): BlogPost {
        return createRandomPost().let {
            it.id = uuid
            it.organizationId = organization
            it
        }
    }
}