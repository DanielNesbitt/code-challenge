package com.genedata.ws

import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.close
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.actor

/**
 * @author Daniel Nesbitt
 */
sealed class ConnectionManagerMsg

class Connect(
    val user: String,
    val ws: WebSocketSession
) : ConnectionManagerMsg()

@Suppress("EXPERIMENTAL_API_USAGE")
fun CoroutineScope.connectionManagerActor() = actor<ConnectionManagerMsg> {
    val connections = mutableMapOf<String, WebSocketSession>()

    suspend fun handleConnect(msg: Connect) {
        connections.remove(msg.user)
            ?.close(CloseReason(4001, "Another connection was made."))
        connections[msg.user] = msg.ws
    }

    for (msg in channel) {
        when (msg) {
            is Connect -> handleConnect(msg)
        }
    }
}

