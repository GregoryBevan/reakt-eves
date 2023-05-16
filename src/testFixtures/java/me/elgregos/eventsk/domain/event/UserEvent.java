package me.elgregos.eventsk.domain.event;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import me.elgregos.eventsk.domain.Event;
import me.elgregos.eventsk.domain.entity.User;

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

        public UserCreated(User user, LocalDateTime createdAt, UUID createdBy) {
            this(UUID.randomUUID(), null, 1, createdAt, createdBy, user.id(), user.toJson());
        }

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