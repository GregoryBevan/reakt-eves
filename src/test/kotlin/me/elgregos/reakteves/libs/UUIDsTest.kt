package me.elgregos.reakteves.libs

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class UUIDsTest {

    @Test
    fun `doit retrouver la date UTC de generation de l'UUID`() {
        val uuidV7 = UUID.fromString("018cd55e-ccfb-782e-b119-161870f565fe")

        assertThat(uuidV7.utcDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 4, 16, 46, 49, 83000000))
    }

    @Test
    fun `doit creer un UUID V5 a partir d'une chaine de caracteres`() {
        repeat(5) {
            assertThat(uuidV5("une chaîne invariante doit produire le même UUID").toString()).isEqualTo("03367dc7-20d5-51d5-b321-9cdd5e36b7f9")
        }
    }

}