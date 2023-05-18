package me.elgregos.reakteves.infrastructure

import me.elgregos.reakteves.domain.FakeEvent
import org.springframework.data.r2dbc.repository.Query
import reactor.core.publisher.Flux
import java.util.*

interface FakeEventRepository: EventEntityRepository<FakeEventEntity, FakeEvent, UUID> {

    @Query(
        """
        select *
        from fake_event e
        where e.aggregate_id = :aggregateId
        order by e.sequence_num     
        """
    )
    override fun findByAggregateId(aggregateId: UUID): Flux<FakeEventEntity>
}