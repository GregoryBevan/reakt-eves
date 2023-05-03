package me.elgregos.eventsk.extension.postgresql

import org.testcontainers.containers.PostgreSQLContainer


class PostgreSQLContainerHandler {

    private lateinit var postgreSQLContainer: PostgreSQLContainer<Nothing>

    fun start() {
        postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:13")
        postgreSQLContainer.start()
        registerSpringProperties()
    }

    fun stop() {
        postgreSQLContainer.stop()
    }

    private fun registerSpringProperties() {
        System.setProperty("spring.r2dbc.url", postgreSQLContainer.jdbcUrl.replace("jdbc", "r2dbc:pool"))
        System.setProperty("spring.r2dbc.username", postgreSQLContainer.username)
        System.setProperty("spring.r2dbc.password", postgreSQLContainer.password)
        System.setProperty("spring.liquibase.change-log", "classpath:db-changelog/changelog-test.sql")
        System.setProperty("spring.liquibase.url", postgreSQLContainer.jdbcUrl)
        System.setProperty("spring.liquibase.user", postgreSQLContainer.username)
        System.setProperty("spring.liquibase.password", postgreSQLContainer.password)
        System.setProperty("database.url", postgreSQLContainer.jdbcUrl)
        System.setProperty("database.username", postgreSQLContainer.username)
        System.setProperty("database.password", postgreSQLContainer.password)
    }
}