package me.elgregos.eventsk.config

import me.elgregos.eventsk.ReactorEventBus
import me.elgregos.eventsk.ReactorEventPublisher
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import java.util.UUID

@SpringBootTest
@ContextConfiguration(classes = [BaseIntegrationTest.TestConfig::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BaseIntegrationTest {

    @TestConfiguration
    class TestConfig {

        @Bean
        fun reactorEventBus() = ReactorEventBus<UUID>()

        @Bean
        fun reactorEventPublisher(reactorEventBus: ReactorEventBus<UUID>) = ReactorEventPublisher(reactorEventBus)
    }

}