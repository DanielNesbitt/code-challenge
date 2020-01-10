package com.genedata.ws

import com.genedata.session.UserSession
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.sessions.get
import io.ktor.sessions.sessions
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

class Connect(
    val user: String,
    val ws: WebSocketServerSession,
    val response: CompletableDeferred<SendChannel<Frame>>
) : ConnectionManagerMsg()


@Suppress("EXPERIMENTAL_API_USAGE")
fun CoroutineScope.connectionManagerActor() = actor<ConnectionManagerMsg> {
    val connections = mutableMapOf<String, WebSocketServerSession>()

    fun handleConnect(msg: Connect) {
        connections[msg.user] = msg.ws
        msg.response.complete()
    }

    for (msg in channel) {
        when (msg) {
            is Connect -> handleConnect(msg)
        }
    }
}


suspend fun DefaultWebSocketServerSession.connect() {
    val session = call.sessions.get<UserSession>()
    if (session == null) {
        close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
    } else {
        val cm = connectionManagerActor()
        val response = CompletableDeferred<Int>()
        cm.send(Connect(session.user, this, response))
        val await = response.await()
        send(Frame.Text("Hi from server"))
        while (true) {
            val frame = incoming.receive()
            if (frame is Frame.Text) {
                send(Frame.Text("Client said: " + frame.readText()))
            }
        }
    }
}
