@param String domainPackage
@param String domain
package ${domainPackage}.domain.event

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.reakteves.domain.event.Event
import java.time.LocalDateTime
import java.util.*

sealed class ${domain}Event(
    id: UUID,
    sequenceNum: Long?,
    version: Int,
    createdAt: LocalDateTime,
    createdBy: UUID,
    aggregateId: UUID,
    eventType: String,
    event: JsonNode
) : Event<UUID, UUID>(
    id, sequenceNum, version, createdAt, createdBy, eventType, aggregateId, event
) {

}