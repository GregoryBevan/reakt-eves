package me.elgregos.reakteves.libs

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

val genericObjectMapper: ObjectMapper = JsonMapper.builder()
    .addModules(KotlinModule.Builder().build(), JavaTimeModule())
    .configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false)
    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
    .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
    .configure(MapperFeature.USE_STD_BEAN_NAMING, true)
    .build()
