package me.sknz.simpleblog.domain.service.sync

import me.sknz.simpleblog.domain.exceptions.SyncUpdatedException
import me.sknz.simpleblog.domain.util.BlogPostCreator
import me.sknz.simpleblog.domain.util.SyncPushObject
import me.sknz.simpleblog.infra.repository.PostRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.kotlin.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*

@ExtendWith(SpringExtension::class)
internal class PostModelSyncTest {

    @Mock
    lateinit var repository: PostRepository
    lateinit var postModelSync: PostModelSync

    private val post = UUID.fromString("c1227dab-283e-495f-85d7-805df4f17141")
    private val organization = UUID.fromString("140499f4-ecb7-4b0e-b4f5-00362da059e9")
    private val user = UUID.fromString("140499f4-ecb7-4b0e-b4f5-00362da079e3")

    @BeforeEach
    fun setUp() {
        val items = Flux.just(BlogPostCreator.createRandomWithId(post, organization), BlogPostCreator.createRandomPost())
        whenever(repository.findAll()).thenReturn(items)
        whenever(repository.findAllById(any<Flux<UUID>>()))
            .then { mock ->
                val uuid = mock.getArgument<Flux<UUID>>(0)
                items.filterWhen { item -> uuid.any { item.id == it } }
            }

        postModelSync = PostModelSync(repository, organization, user, SyncPushObject.getValidChanges().changes.posts)
    }

    @Test
    @DisplayName("Processar todos os elementos criados e retorna-los")
    fun processar_elementos_criados() {
        StepVerifier.create(postModelSync.onProcessCreated())
            .expectNextCount(1)
            .expectComplete()
            .verifyThenAssertThat()
    }

    @Test
    @DisplayName("Processar todos os elementos atualizados sem conflitos")
    fun processar_elementos_atualizados() {
        StepVerifier.create(postModelSync.onProcessUpdated())
            .expectNextCount(1)
            .expectComplete()
            .verifyThenAssertThat()
    }

    @Test
    @DisplayName("Disparar exceção ao tentar processar atualizações invalidas")
    fun tentar_processar_elementos_atualizados_com_conflitos() {
        StepVerifier.create(PostModelSync(repository, organization, user, SyncPushObject.getInvalidChanges()).onProcessUpdated())
            .expectError(SyncUpdatedException::class.java)
            .verifyThenAssertThat()
    }

  /*@Test
    fun onProcessDeleted() {
        // Ao deletar será ignorado elementos invalidos e etc, não precisa de testes.
    }
  */
}