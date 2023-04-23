package me.elgregos.eventsk

interface Command {

    fun type(): String = javaClass.typeName

}
