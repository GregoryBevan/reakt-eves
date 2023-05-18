package me.elgregos.reakteves.config

import me.elgregos.reakteves.TestConfig
import me.elgregos.reakteves.config.postgresql.PostgreSQLExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import java.util.*

@SpringBootTest
@ContextConfiguration(classes = [TestConfig::class])
@ExtendWith(PostgreSQLExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BaseIntegrationTest {

}