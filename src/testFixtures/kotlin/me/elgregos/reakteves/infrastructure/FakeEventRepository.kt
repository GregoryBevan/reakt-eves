package me.elgregos.reakteves.infrastructure

import me.elgregos.reakteves.domain.FakeEvent
import me.elgregos.reakteves.infrastructure.event.EventEntityRepository
import java.util.*

interface FakeEventRepository: EventEntityRepository<FakeEventEntity, FakeEvent, UUID>