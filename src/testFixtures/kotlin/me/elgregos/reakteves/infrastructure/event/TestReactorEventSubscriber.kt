package me.elgregos.reakteves.infrastructure.event

import me.elgregos.reakteves.domain.event.Event
import reactor.core.publisher.Mono
import java.util.*

class TestReactorEventSubscriber(reactorEventBus: ReactorEventBus<UUID, UUID>) :
    ReactorEventSubscriber<UUID, UUID>(reactorEventBus) {

    var events = mutableListOf<Event<UUID, UUID>>()

    override fun onEvent(event: Event<UUID, UUID>) = Mono.just(event)
        .doOnNext { events.add(it) }
        .then()

}