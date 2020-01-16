package com.genedata.messages.generator

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

/**
 * @author Daniel Nesbitt
 */
class ReduxActionSerializer : JsonSerializer<Any?>() {

    override fun serialize(value: Any?, gen: JsonGenerator?, provider: SerializerProvider?) {
        if (value != null) {
            val kClass = value::class
            val serializer = provider?.findValueSerializer(Any::class.java)
            provider?.setAttribute("type", kClass.simpleName)
            serializer?.serialize(value, gen, provider)
        }
    }

}
