@param String domainPackage
@param String domain
@param String domainPrefix
package ${domainPackage}.application

import ${domainPackage}.application.${domain}Command.*
import ${domainPackage}.domain.event.${domain}Aggregate
import ${domainPackage}.domain.event.${domain}Event
import me.elgregos.reakteves.domain.event.EventStore
import me.elgregos.reakteves.infrastructure.event.ReactorEventPublisher
import org.springframework.stereotype.Service
import java.util.*

@Service
class ${domain}CommandHandler(
    val ${domainPrefix}EventStore: EventStore<${domain}Event, UUID, UUID>,
    val ${domainPrefix}EventPublisher: ReactorEventPublisher<UUID, UUID>
) {

    fun handle(${domainPrefix}Command: ${domain}Command) =
        when (${domainPrefix}Command) {
            is Create${domain} -> create${domain}(${domainPrefix}Command)
        }
            .flatMap { ${domainPrefix}EventStore.save(it) }
            .doOnNext { ${domainPrefix}EventPublisher.publish(it) }

    private fun create${domain}(${domainPrefix}Command: Create${domain}) =
        ${domain}Aggregate(${domainPrefix}Command.${domainPrefix}Id, ${domainPrefix}Command.createdBy, ${domainPrefix}EventStore)
            .create${domain}(${domainPrefix}Command.createdAt)

}