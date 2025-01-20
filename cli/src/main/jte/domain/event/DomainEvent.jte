@param String domainPackage
@param String domain
@param String domainPrefix
package ${domainPackage}.domain.event

import com.fasterxml.jackson.databind.JsonNode
import ${domainPackage}.domain.entity.${domain}
import me.elgregos.reakteves.domain.event.Event
import me.elgregos.reakteves.libs.genericObjectMapper
import me.elgregos.reakteves.libs.nowUTC
import java.time.LocalDateTime
import java.util.*

sealed class ${domain}Event(
    id: UUID,
    version: Int,
    createdAt: LocalDateTime,
    createdBy: UUID,
    aggregateId: UUID,
    eventType: String,
    event: JsonNode
) : Event<UUID, UUID>(
    id, version, createdAt, createdBy, eventType, aggregateId, event
) {

    data class ${domain}Created(
        override val id: UUID,
        override val version: Int = 1,
        override val createdAt: LocalDateTime = nowUTC(),
        override val createdBy: UUID,
        val ${domainPrefix}Id: UUID,
        override val event: JsonNode
    ) : ${domain}Event(
        id,
        version,
        createdAt,
        createdBy,
        ${domainPrefix}Id,
        ${domain}Created::class.simpleName!!,
        event
    ) {
        constructor(${domainPrefix}: ${domain}) : this(
            ${domainPrefix}Id = ${domainPrefix}.id,
            createdAt = ${domainPrefix}.createdAt,
            createdBy = ${domainPrefix}.createdBy,
            event = genericObjectMapper.readTree(genericObjectMapper.writeValueAsString(${domainPrefix}))
        )
    }
}