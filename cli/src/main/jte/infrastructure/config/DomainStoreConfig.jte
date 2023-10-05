@param String domainPackage
@param String domain
@param String domainPrefix
package ${domainPackage}.infrastructure.config

import ${domainPackage}.domain.entity.${domain}
import ${domainPackage}.domain.event.${domain}Event
import ${domainPackage}.domain.event.${domain}EventRepository
import ${domainPackage}.infrastructure.event.${domain}EventEntity
import ${domainPackage}.infrastructure.projection.${domain}Entity
import ${domainPackage}.infrastructure.projection.${domain}ProjectionRepository
import me.elgregos.reakteves.domain.event.EventStore
import me.elgregos.reakteves.domain.projection.ProjectionStore
import me.elgregos.reakteves.infrastructure.event.DefaultEventStore
import me.elgregos.reakteves.infrastructure.projection.DefaultProjectionStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID

@Configuration
class ${domain}StoreConfig {

    @Bean
    fun ${domainPrefix}EventStore(${domainPrefix}EventRepository: ${domain}EventRepository): EventStore<${domain}Event, UUID, UUID> =
        DefaultEventStore(${domainPrefix}EventRepository, ${domain}EventEntity::class, ${domain}Event::class)

    @Bean
    fun ${domainPrefix}ProjectionStore(${domainPrefix}ProjectionRepository: ${domain}ProjectionRepository): ProjectionStore<${domain}, UUID, UUID> =
        DefaultProjectionStore(${domainPrefix}ProjectionRepository, ${domain}Entity::class, ${domain}::class)
}