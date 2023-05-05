package me.elgregos.eventsk.config.postgresql

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Define a Junit 5 extension to start and stop a PostgreSQL
 * container before and after class test runs
 */
class PostgreSQLExtension : BeforeAllCallback, AfterAllCallback {

    private val postgreSQLContainerHandler = PostgreSQLContainerHandler()

    override fun beforeAll(context: ExtensionContext?) {
        postgreSQLContainerHandler.start()
    }

    override fun afterAll(context: ExtensionContext?) {
        postgreSQLContainerHandler.stop()
    }
}