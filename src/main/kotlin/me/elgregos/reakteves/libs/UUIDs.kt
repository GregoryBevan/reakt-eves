package me.elgregos.reakteves.libs

import com.github.f4b6a3.uuid.UuidCreator
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

fun uuidV7(): UUID = UuidCreator.getTimeOrderedEpoch()

fun uuidNul(): UUID = UuidCreator.getNil()

fun uuidV5(name: String): UUID = UuidCreator.getNameBasedSha1(name)

fun UUID.utcDateTime(): LocalDateTime =
    this.toString().substring(0, 13).replace("-", "")
        .let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it.toLong(16)), ZoneOffset.UTC) }