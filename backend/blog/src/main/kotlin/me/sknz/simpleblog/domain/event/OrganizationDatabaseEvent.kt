package me.sknz.simpleblog.domain.event

data class OrganizationDatabaseEvent<T> (
    val organization: String,
    val type: String,
    val payload: T,
    val emitId: String?
)