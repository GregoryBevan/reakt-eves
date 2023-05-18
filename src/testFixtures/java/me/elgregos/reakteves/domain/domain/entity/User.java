package me.elgregos.reakteves.domain.domain.entity;

import me.elgregos.reakteves.domain.JsonConvertible;

import java.util.UUID;

public record User(UUID id, String firstName, String lastName) implements JsonConvertible {
}
