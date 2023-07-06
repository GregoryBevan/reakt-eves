package me.elgregos.reakteves.domain.event

import org.springframework.http.HttpStatus

class EventVersionException(message: String, httpStatus: HttpStatus = HttpStatus.BAD_REQUEST): Exception(message)