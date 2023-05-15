package me.elgregos.eventsk.domain.entity;

import me.elgregos.eventsk.domain.JsonConvertible;

import java.util.UUID;

public record User(UUID id, String firstName, String lastName) implements JsonConvertible {
}
