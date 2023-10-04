@param String domainPackage
@param String domain
package ${domainPackage}.infrastructure.event

import ${domainPackage}.domain.event.${domain}Event
import ${domainPackage}.domain.event.${domain}EventRepository
import me.elgregos.reakteves.infrastructure.event.EventEntityRepository
import java.util.*

interface ${domain}EventEntityRepository : EventEntityRepository<${domain}EventEntity, ${domain}Event, UUID, UUID>, ${domain}EventRepository