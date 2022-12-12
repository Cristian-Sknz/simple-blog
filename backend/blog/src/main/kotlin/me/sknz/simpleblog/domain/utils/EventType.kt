package me.sknz.simpleblog.domain.utils

enum class EventType(val type: String) {
    CREATED("created"), UPDATED("updated"), DELETED("deleted")
}