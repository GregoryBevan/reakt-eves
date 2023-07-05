package me.elgregos.reakteves.domain.entity

import me.elgregos.reakteves.domain.JsonConvertible
import java.time.LocalDateTime
import java.util.*

interface DomainEntity: JsonConvertible {
    val id: UUID
    val version: Int
    val createdAt: LocalDateTime
    val createdBy: UUID
    val updatedAt: LocalDateTime
    val updatedBy: UUID
}