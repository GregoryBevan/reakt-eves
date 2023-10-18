package me.elgregos.reakteves.infrastructure;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.elgregos.reakteves.domain.domain.event.UserEvent;
import me.elgregos.reakteves.infrastructure.event.EventEntity;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("user_event")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserEventEntity extends EventEntity<UserEvent, UUID, UUID> {

    protected UserEventEntity() {
        this(null, null, null, null, null, null, null);
    }

    protected UserEventEntity(UUID id,
                              Integer version,
                              LocalDateTime createdAt,
                              UUID createdBy,
                              String eventType,
                              UUID aggregateId,
                              JsonNode event) {
        super(id, version, createdAt, createdBy, eventType, aggregateId, event);
    }

    public static UserEventEntity fromUserEvent(UserEvent userEvent) {
        return new UserEventEntity(
                userEvent.getId(),
                userEvent.getVersion(),
                userEvent.getCreatedAt(),
                userEvent.getCreatedBy(),
                userEvent.getEventType(),
                userEvent.getAggregateId(),
                userEvent.getEvent());
    }
}
