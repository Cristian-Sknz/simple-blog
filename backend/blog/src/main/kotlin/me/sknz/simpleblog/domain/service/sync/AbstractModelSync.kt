package me.sknz.simpleblog.domain.service.sync

import me.sknz.simpleblog.api.request.SyncObjectRequest
import me.sknz.simpleblog.api.request.sync.SyncedModelRequest
import me.sknz.simpleblog.domain.exceptions.SyncUpdatedException
import me.sknz.simpleblog.domain.model.BlogPost
import me.sknz.simpleblog.domain.model.Model
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.util.*

abstract class AbstractModelSync<V: SyncedModelRequest<UUID>, S: Model<UUID>>(
    private val objects: SyncObjectRequest.ChangeObject<V>
) {

    /**
     * Função para processar todos os elementos criados do banco de dados local.
     *
     * Esta função irá aplicar a filtro [onBeforeProcessFilter], em seguida
     * converterá todos os models com [onConvertToDatabaseModel] para o formato aceito pelo banco de dados remoto.
     *
     * Para evitar conflitos de items já existentes, a função irá pegar todos os existentes com [getExists]
     * e irá mesclar com os novos com a função [onMergeWithExists].
     *
     * No final da operação irá retornar um fluxo com todas as modificações feitas
     * que poderá ser salvo no banco de dados remoto.
     */
    fun onProcessCreated(): Flux<S> {
        return objects.created.toFlux()
            .map(this::onConvertToDatabaseModel)
            .filterWhen(this::onBeforeProcessFilter)
            .collectList().zipWhen { list ->
                getExists(list.map { it.id }.toFlux()).collectList()
            }.flatMapMany { tuple ->
                if (tuple.t2.isEmpty()) return@flatMapMany tuple.t1.toFlux()
                tuple.t1.map { t1 ->
                    this.onMergeWithExists(t1, tuple.t2.find { t2 -> t1.id == t2.id } ?: return@map t1)
                }.toFlux()
            }
    }

    /**
     * Função para processar todos os elementos atualizados no banco de dados local.
     *
     * Esta função irá aplicar o filtro [onBeforeProcessFilter], em seguida
     * converterá todos os models com [onConvertToDatabaseModel] para o formato aceito pelo banco de dados remoto.
     *
     * Será feito uma verificação nos itens locais visando a existência dos mesmos no banco de dados remoto (com o [getExists]),
     * caso não existam a exceção [SyncUpdatedException] será disparada. A exceção [SyncUpdatedException] será disparada,
     * pois, não há como atualizar itens que não existem no banco de dados.
     *
     * Caso não ocorra problemas, no final da operação irá retornar um fluxo com todas as modificações feitas
     * que poderá ser salvo no banco de dados remoto.
     *
     * @throws SyncUpdatedException caso não existam os itens que serão modificados no banco de dados
     */
    fun onProcessUpdated(): Flux<S> {
        return objects.updated.toFlux()
            .map(this::onConvertToDatabaseModel)
            .filterWhen(this::onBeforeProcessFilter)
            .collectList().zipWhen { list ->
                getExists(list.map { it.id }.toFlux()).collectList()
            }.filter { tuple ->
                val notexists = tuple.t1.filter { t1 -> tuple.t2.none { t2 -> t1.id == t2.id  } }
                if (notexists.isNotEmpty()) {
                    throw SyncUpdatedException(BlogPost.table, notexists)
                } else true
            }
            .flatMapMany { tuple ->
                tuple.t1.map { t1 -> this.onMergeWithExists(t1, tuple.t2.find { t2 -> t2.id == t1.id }!!) }.toFlux()
            }
    }

    /**
     * Função para processar todos os elementos deletados pelo banco de dados local.
     *
     * Esta função irá pegar todos os items existentes e depois filtra-los
     * utilizando o [onBeforeProcessFilter]. Serão ignorados items
     * que não existam no banco de dados remoto.
     */
    fun onProcessDeleted(): Flux<S> {
        return objects.deleted.toMono()
            .flatMapMany { getExists(it.toFlux()) }
            .filterWhen(this::onBeforeProcessFilter)
    }

    open fun onBeforeProcessFilter(value: S) = Mono.just(true)
    open fun getExists(ids: Flux<UUID>): Flux<S> = Flux.empty()
    abstract fun onMergeWithExists(change: S, original: S): S
    abstract fun onConvertToDatabaseModel(item: V) : S

}