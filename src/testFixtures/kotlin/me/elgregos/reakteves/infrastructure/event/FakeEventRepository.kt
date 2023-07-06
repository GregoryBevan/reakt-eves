package me.elgregos.reakteves.infrastructure.event

import me.elgregos.reakteves.domain.event.FakeEvent
import java.util.*

interface FakeEventRepository: EventEntityRepository<FakeEventEntity, FakeEvent, UUID, UUID>