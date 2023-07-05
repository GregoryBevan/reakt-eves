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
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@SpringBootTest
@ContextConfiguration(classes = [TestConfig::class])
@ExtendWith(PostgreSQLExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BaseIntegrationTest {

    @Autowired
    protected var connectionFactory: ConnectionFactory? = null

    @Autowired
    protected var cleaner: ResourceDatabasePopulator? = null

    @BeforeTest
    fun baseSetUp() {
    }

    @AfterTest
    fun clear() {
        cleaner!!.populate(connectionFactory!!).block()
    }

}