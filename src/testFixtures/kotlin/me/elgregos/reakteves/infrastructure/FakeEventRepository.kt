package me.elgregos.reakteves.infrastructure

import me.elgregos.reakteves.domain.FakeEvent
import java.util.*

interface FakeEventRepository: EventEntityRepository<FakeEventEntity, FakeEvent, UUID>