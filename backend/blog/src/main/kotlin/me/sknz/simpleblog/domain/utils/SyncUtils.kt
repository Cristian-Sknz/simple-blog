package me.sknz.simpleblog.domain.utils

import me.sknz.simpleblog.domain.model.Model
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import java.util.stream.Collectors

class SyncObject(
    val table: String,
    created: Collection<Model<*>>? = null,
    updated: Collection<Model<*>>? = null,
    deleted: Collection<Model<*>>? = null
) {

    var created: Collection<Model<*>>
    var updated: Collection<Model<*>>
    var deleted: Collection<Model<*>>

    init {
        this.created = created ?: emptyList()
        this.updated = updated ?: emptyList()
        this.deleted = deleted ?: emptyList()
    }
}

fun <T : Model<UUID>> getSyncObjectFrom(tablename: String,
                                        created: Flux<T>,
                                        updated: Flux<T>?): Mono<SyncObject> {
    if (updated == null) {
        return created.map { Pair("created", it) }
            .collectMultimap(Pair<String, T>::first, Pair<String, T>::second)
            .toSyncObject(tablename)
    }
    val createdSet = created.collect(Collectors.toSet()).cache()

    return updated.filterWhen { update -> createdSet.map { it.none { it.id == update.id } } }
        .map { Pair("created", it) }
        .mergeWith(createdSet.flatMapMany {
            Flux.fromIterable(it).map { model -> Pair("created", model) }
        })
        .collectMultimap(Pair<String, T>::first, Pair<String, T>::second)
        .toSyncObject(tablename)
}

fun Mono<out Map<String, Collection<Model<*>>>>.toSyncObject(table: String): Mono<SyncObject> {
    return this.map {
        SyncObject(table, it["created"], it["updated"], it["deleted"])
    }
}