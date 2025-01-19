package me.elgregos.reakteves.domain.event

import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDateTime

abstract class Event<ID, UserID>(
    open val id: ID,
    open val version: Int,
    open val createdAt: LocalDateTime,
    open val createdBy: UserID,
    open val eventType: String,
    open val aggregateId: UserID,
    open val event: JsonNode)
