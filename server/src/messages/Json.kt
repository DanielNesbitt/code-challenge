package com.genedata.messages

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.genedata.messages.generator.SocketAction
import com.genedata.messages.generator.SocketActionDeserializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * @author Daniel Nesbitt
 */
object Json {

    fun initializeMapper() {
        val mapper = jacksonObjectMapper()
        val module = SimpleModule("SocketDeserializer")
        module.addDeserializer(SocketAction::class.java, SocketActionDeserializer())
        mapper.registerModule(module)
    }

    suspend fun toJsonString(obj: Any): String {
        return withContext(Dispatchers.IO) {
            val json = jacksonObjectMapper().valueToTree<ObjectNode>(obj)
            json.toString()
        }
    }

}
