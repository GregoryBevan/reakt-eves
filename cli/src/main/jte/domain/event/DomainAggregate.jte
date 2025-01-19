@param String domainPackage
@param String domain
@param String domainPrefix
package ${domainPackage}.domain.event

import ${domainPackage}.domain.entity.${domain}
import ${domainPackage}.domain.event.${domain}Event.*
import me.elgregos.reakteves.domain.event.EventStore
import me.elgregos.reakteves.domain.event.JsonAggregate
import reactor.core.publisher.Flux
import java.util.*

class ${domain}Aggregate(
    ${domainPrefix}Id: UUID, 
    ${domainPrefix}EventStore: EventStore<${domain}Event, UUID, UUID>
) : JsonAggregate<${domain}Event, UUID, UUID>(${domainPrefix}Id, ${domainPrefix}EventStore) {

    fun create${domain}(${domainPrefix}: ${domain}): Flux<${domain}Event> =
        Flux.just(${domain}Created(${domainPrefix})) 
}