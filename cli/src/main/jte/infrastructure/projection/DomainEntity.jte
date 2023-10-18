@param String domainPackage
@param String domain
@param String domainTable
package ${domainPackage}.infrastructure.projection

import com.fasterxml.jackson.databind.JsonNode
import ${domainPackage}.domain.entity.${domain}
import me.elgregos.reakteves.infrastructure.projection.ProjectionEntity
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("${domainTable}")
data class ${domain}Entity(
    @get:JvmName("id") val id: UUID,
    override val version: Int,
    override val createdAt: LocalDateTime,
    override val createdBy: UUID,
    override val updatedAt: LocalDateTime,
    override val updatedBy: UUID,
    override val details: JsonNode
) : ProjectionEntity<${domain}, UUID, UUID>(id, version, createdAt, createdBy, updatedAt, updatedBy, details)