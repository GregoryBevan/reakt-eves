package me.elgregos.eventsk

import org.springframework.http.HttpStatus

class EventVersionException(message: String, httpStatus: HttpStatus = HttpStatus.BAD_REQUEST): Exception(message) {
}