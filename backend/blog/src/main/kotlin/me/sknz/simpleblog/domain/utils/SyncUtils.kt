package me.sknz.simpleblog.domain.utils

import me.sknz.simpleblog.domain.model.DeletedObject
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
    var deleted: Collection<UUID>

    init {
        this.created = created ?: emptyList()
        this.updated = updated ?: emptyList()
        this.deleted = deleted?.map { it.id as UUID } ?: emptyList()
    }
}

fun <T : Model<UUID>> getSyncObjectFrom(tablename: String, created: Flux<T>, updated: Flux<T>?, deletions: Flux<DeletedObject>): Mono<SyncObject> {
    if (updated == null) {
        return created.map { "created" to it }
            .collectMultimap(Pair<String, T>::first, Pair<String, T>::second)
            .toSyncObject(tablename)
    }
    val createdSet = created.collect(Collectors.toSet()).cache()

    return updated.filterWhen { update -> createdSet.map { it.none { it.id == update.id } } }
        .map { "updated" to it as Model<*> }
        .mergeWith(createdSet.flatMapMany {
            Flux.fromIterable(it).map { model -> "created" to model }
        })
        .mergeWith(deletions.filter {
            it.collection.equals(tablename, true)
        }.map { "deleted" to it })
        .collectMultimap(Pair<String, Model<*>>::first, Pair<String, Model<*>>::second)
        .toSyncObject(tablename)
}

fun Mono<out Map<String, Collection<Model<*>>>>.toSyncObject(table: String): Mono<SyncObject> {
    return this.map {
        SyncObject(table, it["created"], it["updated"], it["deleted"])
    }
}