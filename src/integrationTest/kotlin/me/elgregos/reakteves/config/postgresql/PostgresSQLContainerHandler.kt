package me.elgregos.reakteves.config.postgresql

import io.github.oshai.kotlinlogging.KotlinLogging
import org.rnorth.ducttape.unreliables.Unreliables
import org.testcontainers.containers.ContainerLaunchException
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy
import org.testcontainers.delegate.AbstractDatabaseDelegate
import org.testcontainers.ext.ScriptUtils
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import java.util.concurrent.TimeUnit


private val logger = KotlinLogging.logger {}

class PostgresSQLContainerHandler {

    companion object {
        val postgresSQLContainer: PostgreSQLContainer<Nothing> = PostgreSQLContainer<Nothing>("postgres:16")
            .waitingFor(PostgresSQLWaitStrategy())
    }

    fun start() {
        postgresSQLContainer.start()
        registerSpringProperties()
    }

    fun stop() {
        postgresSQLContainer.stop()
    }

    private fun registerSpringProperties() {
        System.setProperty("spring.r2dbc.url", postgresSQLContainer.jdbcUrl.replace("jdbc", "r2dbc:pool"))
        System.setProperty("spring.r2dbc.username", postgresSQLContainer.username)
        System.setProperty("spring.r2dbc.password", postgresSQLContainer.password)
        System.setProperty("spring.liquibase.change-log", "classpath:db-changelog/changelog-test.sql")
        System.setProperty("spring.liquibase.url", postgresSQLContainer.jdbcUrl)
        System.setProperty("spring.liquibase.user", postgresSQLContainer.username)
        System.setProperty("spring.liquibase.password", postgresSQLContainer.password)
        System.setProperty("database.url", postgresSQLContainer.jdbcUrl)
        System.setProperty("database.username", postgresSQLContainer.username)
        System.setProperty("database.password", postgresSQLContainer.password)
    }

    class PostgresSQLWaitStrategy : AbstractWaitStrategy() {

        override fun waitUntilReady() {
            try {
                Unreliables.retryUntilSuccess(startupTimeout.seconds.toInt(), TimeUnit.SECONDS) {
                    rateLimiter.doWhenReady {
                        TestContainerPostgresSQLDelegate().execute(
                            "select 555", "", 1,
                            continueOnError = false,
                            ignoreFailedDrops = false
                        )
                    }
                }
            } catch (e: Exception) {
                throw ContainerLaunchException("Timed out waiting for PostgresSQL to be accessible for query execution", e)
            }
        }
    }

    class TestContainerPostgresSQLDelegate : AbstractDatabaseDelegate<Connection>() {

        override fun createNewConnection() =
            DriverManager.getConnection(
                postgresSQLContainer.jdbcUrl,
                Properties().also {
                    it["user"] = postgresSQLContainer.username
                    it["password"] = postgresSQLContainer.password
                })

        override fun execute(
            statement: String,
            scriptPath: String,
            lineNumber: Int,
            continueOnError: Boolean,
            ignoreFailedDrops: Boolean,
        ) {
            val result = getConnection().prepareStatement(statement).executeQuery()
            result.next()
            if (result.getInt(1) == (555)) {
                logger.debug { "Statement $statement was applied" }
            } else {
                throw ScriptUtils.ScriptStatementFailedException(statement, lineNumber, scriptPath)
            }
        }

        override fun closeConnectionQuietly(connection: Connection?) {
            connection?.close() ?: logger.error { "Unable to close connection" }
        }

    }

}