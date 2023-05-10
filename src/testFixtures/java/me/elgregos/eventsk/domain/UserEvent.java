package me.elgregos.eventsk.domain;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
public sealed abstract class UserEvent extends Event<UUID> {

    protected UserEvent(UUID id,
                        Long sequenceNum,
                        Integer version,
                        LocalDateTime createdAt,
                        UUID aggregateId,
                        String eventType,
                        UUID createdBy,
                        JsonNode event) {
        super(id, sequenceNum, version, createdAt, createdBy, eventType, aggregateId, event);
    }

    @EqualsAndHashCode(callSuper = true)
    public static final class UserCreated extends UserEvent {

        public UserCreated(UUID id,
                           Long sequenceNum,
                           Integer version,
                           LocalDateTime createdAt,
                           UUID createdBy,
                           UUID aggregateId,
                           JsonNode event) {
            super(id, sequenceNum, version, createdAt, createdBy, UserCreated.class.getSimpleName(), aggregateId, event);
        }

    }

}
