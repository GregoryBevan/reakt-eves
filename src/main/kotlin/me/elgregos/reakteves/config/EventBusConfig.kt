package me.elgregos.reakteves.config

import me.elgregos.reakteves.infrastructure.event.ReactorEventBus
import me.elgregos.reakteves.infrastructure.event.ReactorEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class EventBusConfig {

    @Bean
    fun reactorEventBus() = ReactorEventBus<UUID, UUID>()

    @Bean
    fun reactorEventPublisher(reactorEventBus: ReactorEventBus<UUID, UUID>): ReactorEventPublisher<UUID, UUID> =
        ReactorEventPublisher(reactorEventBus)

}
