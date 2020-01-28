package com.genedata.messages.generator

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.genedata.messages.Answer
import java.io.IOException

class SocketActionDeserializer : JsonDeserializer<SocketAction?>() {
    @Throws(IOException::class, JsonMappingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext?): SocketAction {
        val codec = jp.codec
        val node = codec.readTree<JsonNode>(jp)
        val type: String = node.get("type").asText()
        return when (type) {
            "Answer" -> codec.treeToValue(node, Answer::class.java)
            else -> throw JsonMappingException(
                jp,
                "Invalid value for the \"type\" property"
            )
        }
    }
}
