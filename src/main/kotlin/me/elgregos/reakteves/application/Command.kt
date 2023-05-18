package me.elgregos.reakteves.application

interface Command {

    fun type(): String = javaClass.typeName

}
