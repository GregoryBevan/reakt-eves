package me.elgregos.reakteves.infrastructure.projection

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.reakteves.domain.entity.*
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("fake")
data class FakeProjectionEntity(
    @get:JvmName("id") val id: UUID,
    override val sequenceNum: Long? = null,
    override val version: Int,
    override val createdAt: LocalDateTime,
    override val createdBy: UUID,
    override val updatedAt: LocalDateTime,
    override val updatedBy: UUID,
    override val details: JsonNode
) : ProjectionEntity<FakeDomainEntity, UUID, UUID>(id, sequenceNum, version, createdAt, createdBy, updatedAt, updatedBy, details)


val fakeProjectionEntity: FakeProjectionEntity = FakeProjectionEntity(fakeDomainEntityId, null,1, fakeDomainEntityCreatedAt, fakeDomainEntityCreatedById,  fakeDomainEntityCreatedAt, fakeDomainEntityCreatedById, fakeDomainEntityJson)