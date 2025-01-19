package me.elgregos.reakteves.infrastructure.projection

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.reakteves.domain.JsonConvertible
import me.elgregos.reakteves.domain.entity.DomainEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


open class ProjectionEntity<DE : DomainEntity<ID, UserID>, ID, UserID>(
    @Id private val id: ID,
    open val version: Int,
    open val createdAt: LocalDateTime,
    open val createdBy: UserID,
    open val updatedAt: LocalDateTime,
    open val updatedBy: UserID,
    open val details: JsonNode
) : Persistable<ID> {

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
        fun <DE : DomainEntity<ID, UserID>, PE : ProjectionEntity<DE, ID, UserID>, ID, UserID> fromDomainEntity(
            domainEntity: DE,
            projectionEntityClass: KClass<PE>,
        ): PE = projectionEntityClass.primaryConstructor?.call(
            domainEntity.id,
            domainEntity.version,
            domainEntity.createdAt,
            domainEntity.createdBy,
            domainEntity.updatedAt,
            domainEntity.updatedBy,
            domainEntity.toJson()
        ) as PE
    }
}
