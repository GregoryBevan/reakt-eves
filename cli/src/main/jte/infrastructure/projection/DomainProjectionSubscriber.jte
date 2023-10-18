@param String domainPackage
@param String domain
@param String domainPrefix
package ${domainPackage}.infrastructure.projection

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import ${domainPackage}.domain.entity.${domain}
import ${domainPackage}.domain.event.${domain}Event
import ${domainPackage}.domain.event.${domain}Event.*
import me.elgregos.reakteves.domain.JsonConvertible.Companion.fromJson
import me.elgregos.reakteves.domain.event.Event
import me.elgregos.reakteves.domain.projection.ProjectionStore
import me.elgregos.reakteves.infrastructure.event.ReactorEventBus
import me.elgregos.reakteves.infrastructure.event.ReactorEventSubscriber
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

private val logger = KotlinLogging.logger {}

@Component
class ${domain}ProjectionSubscriber(
    reactorEventBus: ReactorEventBus<UUID, UUID>,
    private val ${domainPrefix}ProjectionStore: ProjectionStore<${domain}, UUID, UUID>,
) : ReactorEventSubscriber<UUID, UUID>(reactorEventBus) {

    @PostConstruct
    fun initialize() {
        subscribe()
    }

    override fun onEvent(event: Event<UUID, UUID>): Mono<Void> {
        return Mono.just(event)
            .filter { e -> e is ${domain}Event }
            .cast(${domain}Event::class.java)
            .flatMap {
                when (it) {
                    is ${domain}Created -> create${domain}(it)
                }
            }
            .doOnError { error -> logger.error(error) { "An error occurred while processing event" } }
            .then()
    }

    private fun create${domain}(event: ${domain}Created) =
        ${domainPrefix}ProjectionStore.insert(fromJson(event.event))
}