package me.elgregos.reakteves.domain

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import me.elgregos.reakteves.libs.genericObjectMapper
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class JsonAggregate<E : Event<IdType>, IdType>(
    aggregateId: IdType,
    eventStore: EventStore<E, IdType>
) : Aggregate<E, IdType> {

    protected val events: Flux<E> = eventStore.loadAllEvents(aggregateId)
    private val lastVersion: Mono<Int> =
        events.hasElements().flatMap {
            if (it) events.last().map { event -> event.version }
            else Mono.just(0)
        }
    private val newEvents = mutableListOf<E>()

    /**
     * Add new event to the aggregate after event version checking
     */
    override fun applyNewEvent(event: E): Mono<E> =
        lastVersion.flatMap { nextVersion() }
            .flatMap {
                if (event.version != it) Mono.error(EventVersionException("New event version (${event.version}) does not match expected next version ($it)"))
                else Mono.just(event)
            }
            .doOnNext {
                newEvents.add(event)
            }

    /**
     * Rebuild the state of the Aggregate from its stored events and new events
     */
    fun previousState(): Mono<JsonNode> =
        Flux.concat(events, Flux.fromIterable(newEvents))
            .reduce(genericObjectMapper.createObjectNode() as JsonNode) { agg, event ->
                JsonMergePatch.fromJson(event.event).apply(agg)
            }


    /**
     * Returns the next version according to last version stored and the number
     * of new events
     */
    fun nextVersion(): Mono<Int> = lastVersion.map { it + newEvents.size + 1 }

    /**
     * Returns immutable list of new events to store and publish
     */
    fun newEvents() = newEvents.toList()
}