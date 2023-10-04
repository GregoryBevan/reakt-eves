@param String domainPackage
@param String domain
@param String domainTable
package ${domainPackage}.infrastructure.event

import com.fasterxml.jackson.databind.JsonNode
import ${domainPackage}.domain.event.${domain}Event
import me.elgregos.reakteves.infrastructure.event.EventEntity
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("${domainTable}_event")
data class ${domain}EventEntity(
    @get:JvmName("id") val id: UUID,
    override val sequenceNum: Long? = null,
    override val version: Int = 1,
    override val createdAt: LocalDateTime,
    override val createdBy: UUID,
    override val eventType: String,
    override val aggregateId: UUID = UUID.randomUUID(),
    override val event: JsonNode
) : EventEntity<${domain}Event, UUID, UUID>(
    id,
    sequenceNum,
    version,
    createdAt,
    createdBy,
    eventType,
    aggregateId,
    event
)