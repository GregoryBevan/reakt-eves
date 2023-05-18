package me.elgregos.reakteves.infrastructure;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.elgregos.reakteves.domain.domain.event.UserEvent;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("user_event")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserEventEntity extends EventEntity<UserEvent, UUID> {

    protected UserEventEntity() {
        this(null, null, null, null, null, null, null, null);
    }

    protected UserEventEntity(UUID id,
                              Long sequenceNum,
                              Integer version,
                              LocalDateTime createdAt,
                              UUID createdBy,
                              String eventType,
                              UUID aggregateId,
                              JsonNode event) {
        super(id, sequenceNum, version, createdAt, createdBy, eventType, aggregateId, event);
    }

    public static UserEventEntity fromUserEvent(UserEvent userEvent) {
        return new UserEventEntity(
                userEvent.getId(),
                userEvent.getSequenceNum(),
                userEvent.getVersion(),
                userEvent.getCreatedAt(),
                userEvent.getCreatedBy(),
                userEvent.getEventType(),
                userEvent.getAggregateId(),
                userEvent.getEvent());
    }
}
