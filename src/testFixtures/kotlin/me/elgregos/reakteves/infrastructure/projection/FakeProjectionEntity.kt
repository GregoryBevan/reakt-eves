package me.elgregos.reakteves.infrastructure.projection

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.reakteves.domain.entity.FakeDomainEntity
import me.elgregos.reakteves.domain.entity.fakeDomainEntityCreatedAt
import me.elgregos.reakteves.domain.entity.fakeDomainEntityId
import me.elgregos.reakteves.domain.entity.fakeDomainEntityJson
import java.time.LocalDateTime
import java.util.*

data class FakeProjectionEntity(
    @get:JvmName("id") val id: UUID,
    override val sequenceNum: Long? = null,
    override val version: Int,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime,
    override val details: JsonNode
) : ProjectionEntity<FakeDomainEntity>(id, sequenceNum, version, createdAt, updatedAt, details)


val fakeProjectionEntity: FakeProjectionEntity = FakeProjectionEntity(fakeDomainEntityId, null,1, fakeDomainEntityCreatedAt, fakeDomainEntityCreatedAt, fakeDomainEntityJson)