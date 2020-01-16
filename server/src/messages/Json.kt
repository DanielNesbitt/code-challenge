package com.genedata.messages

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.genedata.messages.generator.ReduxAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.full.findAnnotation

/**
 * @author Daniel Nesbitt
 */
object Json {

    suspend fun toJsonString(obj: Any): String {
        return withContext(Dispatchers.IO) {
            val json = jacksonObjectMapper().valueToTree<ObjectNode>(obj)
            val reduxAction = obj::class.findAnnotation<ReduxAction>()
            if (reduxAction != null) {
                json.put("type", obj.javaClass.simpleName)
            }
            json.toString()
        }

    }
}
