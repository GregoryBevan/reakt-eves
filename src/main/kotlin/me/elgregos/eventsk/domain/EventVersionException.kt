package me.elgregos.eventsk.domain

import org.springframework.http.HttpStatus

class EventVersionException(message: String, httpStatus: HttpStatus = HttpStatus.BAD_REQUEST): Exception(message)