package com.genedata.ws

import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.WebSocketServerSession
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor

/**
 * @author Daniel Nesbitt
 */

sealed class ConnectionManagerMsg

// TODO send something smaller than the whole session
class Connect(
    val user: String,
    val ws: WebSocketServerSession,
    val response: CompletableDeferred<ConnectionResult>
) : ConnectionManagerMsg()

sealed class ConnectionResult : ConnectionManagerMsg()
object UserAlreadyConnected : ConnectionResult()
// TODO produce a better interface for the external channel used for receiving the ws frames
class Connected(val input: SendChannel<Frame>) : ConnectionResult()


@Suppress("EXPERIMENTAL_API_USAGE")
fun CoroutineScope.connectionManagerActor() = actor<ConnectionManagerMsg> {
    val connections = mutableMapOf<String, WebSocketServerSession>()

    fun handleConnect(msg: Connect) {
        connections[msg.user] = msg.ws
        msg.response.complete(Connected(msg.ws.outgoing))
    }

    for (msg in channel) {
        when (msg) {
            is Connect -> handleConnect(msg)
        }
    }
}

suspend fun DefaultWebSocketServerSession.handleConnection(user: String, cm: SendChannel<ConnectionManagerMsg>) {
    val awaitConnection = CompletableDeferred<ConnectionResult>()
    cm.send(Connect(user, this, awaitConnection))
    when (val result = awaitConnection.await()) {
        is UserAlreadyConnected -> {
            close(CloseReason(4001, "User already connected."))
        }
        is Connected -> {
            for (frame in incoming) {
                result.input.send(frame)
            }
        }
    }
}
