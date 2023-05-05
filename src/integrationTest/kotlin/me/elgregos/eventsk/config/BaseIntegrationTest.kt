package me.elgregos.eventsk.config

import me.elgregos.eventsk.TestConfig
import me.elgregos.eventsk.config.postgresql.PostgreSQLExtension
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