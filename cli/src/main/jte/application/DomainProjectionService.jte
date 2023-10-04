@param String domainPackage
@param String domain
@param String domainPrefix
package ${domainPackage}.application

import ${domainPackage}.domain.entity.${domain}
import me.elgregos.reakteves.domain.projection.ProjectionStore
import org.springframework.stereotype.Service
import java.util.*

@Service
class ${domain}ProjectionService(
    private val ${domainPrefix}ProjectionStore: ProjectionStore<${domain}, UUID, UUID>) {

    fun ${domainPrefix}s() =
        ${domainPrefix}ProjectionStore.list()

    fun ${domainPrefix}(${domainPrefix}Id: UUID) =
        ${domainPrefix}ProjectionStore.find(${domainPrefix}Id)
}