package com.genedata.messages

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * @author Daniel Nesbitt
 */
object Json {

    fun initializeMapper() {
        val mapper = jacksonObjectMapper()
    }

    suspend fun toJsonString(obj: Any): String {
        return withContext(Dispatchers.IO) {
            val json = jacksonObjectMapper().valueToTree<ObjectNode>(obj)
            json.toString()
        }
    }

}
