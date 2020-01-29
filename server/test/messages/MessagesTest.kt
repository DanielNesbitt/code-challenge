package com.genedata.messages

import com.fasterxml.jackson.annotation.JsonSubTypes
import org.junit.Test
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.findAnnotation
import kotlin.test.assertTrue

/**
 * @author Daniel Nesbitt
 */
class MessagesTest {

    @Test
    fun testSocketActionDeserialization() {
        RPC_CLASSES.forEach { rpcClass ->
            if (rpcClass.allSuperclasses.contains(SocketAction::class)) {
                val annotation = SocketAction::class.findAnnotation<JsonSubTypes>()
                val isProperlyAnnotated =
                    annotation!!.value.any { it.value == rpcClass && it.name == rpcClass.simpleName }
                assertTrue(
                    isProperlyAnnotated,
                    "Class sub-type ${rpcClass.simpleName} missing or improperly annotated in ReduxAction."
                )
            }
        }
    }

}
