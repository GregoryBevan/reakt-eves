package me.elgregos.reakteves.domain.entity

import me.elgregos.reakteves.domain.JsonConvertible
import java.time.LocalDateTime

interface DomainEntity<ID, UserID>: JsonConvertible {
    val id: ID
    val version: Int
    val createdAt: LocalDateTime
    val createdBy: UserID
    val updatedAt: LocalDateTime
    val updatedBy: UserID
}