package me.sknz.simpleblog.api.request.sync

import me.sknz.simpleblog.domain.model.BlogUser
import java.util.*

class SyncedUserRequest: SyncedModelRequest<UUID>() {

    override lateinit var id: UUID
    lateinit var name: String
    lateinit var username: String
    lateinit var email: String
    var image: String? = null

    override fun getTable() = BlogUser.table
}