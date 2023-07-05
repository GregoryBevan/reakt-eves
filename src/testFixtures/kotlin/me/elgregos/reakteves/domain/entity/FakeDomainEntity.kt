package me.elgregos.reakteves.domain.entity

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.reakteves.libs.genericObjectMapper
import java.time.LocalDateTime
import java.util.*

val fakeDomainEntityCreatedAt: LocalDateTime = LocalDateTime.of(2023, 7, 5, 9, 0)
val fakeDomainEntityId = UUID.fromString("d79474df-bcc3-4c47-ae3e-abeff649cc80")

data class FakeDomainEntity(
    override val id: UUID = fakeDomainEntityId,
    override val version: Int = 1,
    override val createdAt: LocalDateTime = fakeDomainEntityCreatedAt,
    override val createdBy: UUID = UUID.fromString("c83e4e3d-0d8b-4048-9721-7e2db70bbc57"),
    override val updatedAt: LocalDateTime = fakeDomainEntityCreatedAt,
    override val updatedBy: UUID = UUID.fromString("c83e4e3d-0d8b-4048-9721-7e2db70bbc57"),
    val customProperty: String = "fake-fake-fake"
): DomainEntity

val fakeDomainEntityJson: JsonNode = genericObjectMapper.createObjectNode()
    .put("id", "d79474df-bcc3-4c47-ae3e-abeff649cc80")
    .put("version", 1)
    .put("createdAt", "2023-07-05T09:00:00")
    .put("createdBy", "c83e4e3d-0d8b-4048-9721-7e2db70bbc57")
    .put("updatedAt", "2023-07-05T09:00:00")
    .put("updatedBy", "c83e4e3d-0d8b-4048-9721-7e2db70bbc57")
    .put("customProperty", "fake-fake-fake")
