@param String domainPackage
@param String domain
package ${domainPackage}.domain.entity

import me.elgregos.reakteves.domain.entity.DomainEntity
import java.time.LocalDateTime
import java.util.*

data class ${domain}(
    override val id: UUID,
    override val version: Int = 1,
    override val createdAt: LocalDateTime,
    override val createdBy: UUID,
    override val updatedAt: LocalDateTime = createdAt,
    override val updatedBy: UUID = createdBy,
) : DomainEntity<UUID, UUID> {

}