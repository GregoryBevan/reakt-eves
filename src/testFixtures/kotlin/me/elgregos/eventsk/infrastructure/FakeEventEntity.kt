package me.elgregos.eventsk.infrastructure

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.eventsk.domain.FakeEvent
import me.elgregos.eventsk.libs.nowUTC
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "fake_event")
data class FakeEventEntity(
    @get:JvmName("id") val id: UUID,
    override val sequenceNum: Long? = null,
    override val version: Int = 1,
    override val createdAt: LocalDateTime = nowUTC(),
    override val createdBy: UUID,
    override val eventType:String,
    override val aggregateId: UUID = UUID.randomUUID(),
    override val event: JsonNode
) : EventEntity<FakeEvent, UUID>(
    id,
    sequenceNum,
    version,
    createdAt,
    createdBy,
    eventType,
    aggregateId,
    event
)