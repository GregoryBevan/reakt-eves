@param String domainPackage
@param String domain
package ${domainPackage}.infrastructure.projection

import ${domainPackage}.domain.entity.${domain}
import me.elgregos.reakteves.infrastructure.projection.ProjectionRepository
import java.util.*

interface ${domain}ProjectionRepository : ProjectionRepository<${domain}Entity, ${domain}, UUID, UUID>