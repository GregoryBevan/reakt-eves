package me.elgregos.reakteves.infrastructure.projection

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.reakteves.domain.JsonConvertible
import me.elgregos.reakteves.domain.entity.DomainEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


open class ProjectionEntity<DE : DomainEntity>(
    @Id private val id: UUID,
    open val sequenceNum: Long? = null,
    open val version: Int,
    open val createdAt: LocalDateTime,
    open val updatedAt: LocalDateTime,
    open val details: JsonNode
) : Persistable<UUID> {

    @Transient
    private var isNew = false

    override fun getId() = id

    @Transient
    override fun isNew() = isNew

    fun markNew() {
        isNew = true
    }

    fun toDomainEntity(type: KClass<DE>) =
        JsonConvertible.fromJson(details, type.java)

    companion object {
        fun <DE : DomainEntity, PE : ProjectionEntity<DE>> fromDomainEntity(
            domainEntity: DE,
            projectionEntityClass: KClass<PE>,
            sequenceNum: Long? = null
        ): PE = projectionEntityClass.primaryConstructor?.call(
                domainEntity.id,
                sequenceNum,
                domainEntity.version,
                domainEntity.createdAt,
                domainEntity.updatedAt,
                domainEntity.toJson()
            ) as PE
    }
}
