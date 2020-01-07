package com.genedata.ws

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.actor

/**
 * @author Daniel Nesbitt
 */

sealed class ConnectionManagerMsg
class Connect(val user: String) : ConnectionManagerMsg()

fun CoroutineScope.connectionManagerActor() = actor<ConnectionManagerMsg> {
    val connections = mutableMapOf<String, String>()

    fun handleConnect(msg: Connect) {
        connections.put(msg.user, msg.user)
    }

    for (msg in channel) {
        when(msg) {
            is Connect -> handleConnect(msg)
        }
    }
}
