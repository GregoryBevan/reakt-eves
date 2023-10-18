package me.elgregos.reakteves.domain

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.reakteves.libs.genericObjectMapper

interface JsonConvertible {

    fun toJson(): JsonNode {
        return try {
            genericObjectMapper.readTree(genericObjectMapper.writeValueAsString(this))
        } catch (e: JsonProcessingException) {
            throw RuntimeException("An error occurred during conversion to json", e)
        }
    }

    companion object {

        internal fun <T> fromJson(json: JsonNode, expectedClass: Class<T>): T {
            return try {
                genericObjectMapper.readValue(json.toString(), expectedClass)
            } catch (e: JsonProcessingException) {
                throw RuntimeException("An error occurred during conversion from json", e)
            }
        }

        inline fun <reified T> fromJson(json: JsonNode): T {
            return try {
                genericObjectMapper.readValue(json.toString(), T::class.java)
            } catch (e: JsonProcessingException) {
                throw RuntimeException("An error occurred during conversion from json", e)
            }
        }
    }
}
