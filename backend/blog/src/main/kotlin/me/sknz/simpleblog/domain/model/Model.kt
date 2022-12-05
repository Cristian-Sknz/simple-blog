package me.sknz.simpleblog.domain.model

interface Model<T> {

    var id: T

    fun getTable(): String

}