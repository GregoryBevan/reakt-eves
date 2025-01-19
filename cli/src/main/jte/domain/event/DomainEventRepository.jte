@param String domainPackage
@param String domain
package ${domainPackage}.domain.event

import ${domainPackage}.infrastructure.event.${domain}EventEntity
import me.elgregos.reakteves.infrastructure.event.EventEntityRepository
import java.util.*

interface ${domain}EventRepository : EventEntityRepository<${domain}EventEntity, ${domain}Event, UUID, UUID>