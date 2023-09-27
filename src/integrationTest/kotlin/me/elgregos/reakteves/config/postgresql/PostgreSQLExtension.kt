package me.elgregos.reakteves.config.postgresql

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Define a Junit 5 extension to start and stop a PostgreSQL
 * container before and after class test runs
 */
class PostgreSQLExtension : BeforeAllCallback, AfterAllCallback {

    private val postgresSQLContainerHandler = PostgresSQLContainerHandler()

    override fun beforeAll(context: ExtensionContext?) {
        postgresSQLContainerHandler.start()
    }

    override fun afterAll(context: ExtensionContext?) {
        postgresSQLContainerHandler.stop()
    }
}