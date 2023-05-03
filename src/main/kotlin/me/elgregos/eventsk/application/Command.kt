package me.elgregos.eventsk.application

interface Command {

    fun type(): String = javaClass.typeName

}
