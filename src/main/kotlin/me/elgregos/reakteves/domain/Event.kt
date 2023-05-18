package me.elgregos.reakteves.domain

import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDateTime
import java.util.*

abstract class Event<IdType>(
    open val id: UUID,
    open val sequenceNum: Long?,
    open val version: Int,
    open val createdAt: LocalDateTime,
    open val createdBy: IdType,
    open val eventType: String,
    open val aggregateId: IdType,
    open val event: JsonNode)
