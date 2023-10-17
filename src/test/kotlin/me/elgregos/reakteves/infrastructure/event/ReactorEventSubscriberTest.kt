package me.elgregos.reakteves.infrastructure.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.mockk
import me.elgregos.reakteves.domain.entity.FakeDomainEntity
import me.elgregos.reakteves.domain.entity.fakeDomainEntityCreatedById
import me.elgregos.reakteves.domain.entity.fakeDomainEntityId
import me.elgregos.reakteves.domain.event.fakeUpdatedEvent
import me.elgregos.reakteves.libs.genericObjectMapper
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class ReactorEventSubscriberTest {

    private lateinit var testReactorEventSubscriber: TestReactorEventSubscriber

    @BeforeTest
    fun setUp() {
        testReactorEventSubscriber = TestReactorEventSubscriber(mockk())
    }

    @Test
    fun `should merge event to previous domain entity state`() {
        val previous = FakeDomainEntity()
        val fakeUpdatedEvent = fakeUpdatedEvent(
            UUID.randomUUID(),
            2,
            fakeDomainEntityCreatedById,
            fakeDomainEntityId,
            genericObjectMapper.createObjectNode()
                .put("version", "2")
                .put("customProperty", "updatedValue")
        )

        assertThat(testReactorEventSubscriber.mergeJsonPatch(previous, fakeUpdatedEvent)).isEqualTo(
            previous.copy(
                version = 2,
                customProperty= "updatedValue"
            )
        )
    }
}