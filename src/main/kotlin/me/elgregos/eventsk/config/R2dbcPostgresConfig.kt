package me.elgregos.eventsk.config

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.r2dbc.postgresql.codec.Json
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions

@Configuration
class R2dbcPostgresConfig(
    val connectionFactory: ConnectionFactory,
    val objectMapper: ObjectMapper
) : AbstractR2dbcConfiguration() {

    override fun connectionFactory(): ConnectionFactory {
        return connectionFactory
    }

    @Bean
    override fun r2dbcCustomConversions(): R2dbcCustomConversions {
        val converters = ArrayList<Converter<*, *>?>()
        converters.add(JsonNodeToJsonConverter())
        converters.add(JsonToJsonNodeConverter(objectMapper))
        return R2dbcCustomConversions(storeConversions, converters)
    }
}


@WritingConverter
class JsonNodeToJsonConverter : Converter<JsonNode, Json> {
    override fun convert(jsonNode: JsonNode): Json = Json.of(jsonNode.toString())
}


@ReadingConverter
class JsonToJsonNodeConverter(private val objectMapper: ObjectMapper) : Converter<Json, JsonNode> {
    override fun convert(json: Json): JsonNode = objectMapper.readTree(json.asArray())
}


