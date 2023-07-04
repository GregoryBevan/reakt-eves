package me.elgregos.reakteves.infrastructure.event;

import com.fasterxml.jackson.databind.JsonNode;
import me.elgregos.reakteves.domain.domain.event.UserEvent;
import me.elgregos.reakteves.infrastructure.UserEventEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static me.elgregos.reakteves.domain.domain.event.UserEvent.UserCreated;
import static me.elgregos.reakteves.libs.DatesKt.nowUTC;
import static me.elgregos.reakteves.libs.JsonsKt.getGenericObjectMapper;

class UserEventEntityTest {

 @Test
 void shouldConvertEventEntityToEvent() {
  UUID eventId = UUID.randomUUID();
  JsonNode event = getGenericObjectMapper().createObjectNode().put("name", "machin");
  UserEvent userEvent = new UserCreated(eventId, 1L, 1, nowUTC(), UUID.randomUUID(), UUID.randomUUID(), event);
  UserEventEntity userEventEntity = UserEventEntity.fromUserEvent(userEvent);
  Assertions.assertEquals(userEventEntity.toEvent(UserEvent.class, UUID.class), userEvent);
 }
}
