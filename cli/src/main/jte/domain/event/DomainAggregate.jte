@param String domainPackage
@param String domain
@param String domainPrefix
package ${domainPackage}.domain.event

import ${domainPackage}.domain.event.${domain}Event.*
import me.elgregos.reakteves.domain.event.EventStore
import me.elgregos.reakteves.domain.event.JsonAggregate
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.util.*

class ${domain}Aggregate(
    private val ${domainPrefix}Id: UUID,
    private val userId: UUID,
    ${domainPrefix}EventStore: EventStore<${domain}Event, UUID, UUID>
) :
    JsonAggregate<${domain}Event, UUID, UUID>(${domainPrefix}Id, ${domainPrefix}EventStore) {

    fun create${domain}(createdAt: LocalDateTime): Flux<${domain}Event> =
        Flux.just(${domain}Created(${domainPrefix}Id, userId, createdAt))
}