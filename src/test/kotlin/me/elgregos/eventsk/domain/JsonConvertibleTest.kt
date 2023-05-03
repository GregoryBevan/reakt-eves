package me.elgregos.eventsk.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.eventsk.libs.genericObjectMapper
import java.time.LocalDate
import kotlin.test.Test


class JsonConvertibleTest {

    @Test
    fun `should convert to json`() {
        assertThat(User().toJson()).isEqualTo(userJson)
    }

    @Test
    fun `should convert json to object`() {
        assertThat(JsonConvertible.fromJson(userJson, User::class.java)).isEqualTo(User())
    }

    data class User(
        val firstName: String = "Sarah",
        val lastName: String = "Joute",
        val email: String = "sarah.joute@gmail.com",
        val dateOfBirth: LocalDate = LocalDate.of(2023, 4, 18)
    ): JsonConvertible

    val userJson = genericObjectMapper.createObjectNode()
        .put("firstName", "Sarah")
        .put("lastName", "Joute")
        .put("email", "sarah.joute@gmail.com")
        .put("dateOfBirth", "2023-04-18")


}