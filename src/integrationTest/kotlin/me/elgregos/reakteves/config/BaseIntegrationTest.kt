package me.elgregos.reakteves.config

import io.r2dbc.spi.ConnectionFactory
import me.elgregos.reakteves.TestConfig
import me.elgregos.reakteves.config.postgresql.PostgreSQLExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@SpringBootTest
@ContextConfiguration(classes = [TestConfig::class])
@ExtendWith(PostgreSQLExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BaseIntegrationTest {

    @Autowired
    protected lateinit var connectionFactory: ConnectionFactory

    @Autowired
    protected lateinit var cleaner: ResourceDatabasePopulator

    @BeforeTest
    fun baseSetUp() {
    }

    @AfterTest
    fun clear() {
        cleaner.populate(connectionFactory).block()
    }

}