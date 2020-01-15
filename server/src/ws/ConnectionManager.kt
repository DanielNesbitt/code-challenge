package com.genedata.ws

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.genedata.messages.QuestionEntry
import com.genedata.messages.QuestionsResponse
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.send
import io.ktor.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.WebSocketServerSession
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.withContext

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

    suspend fun handleConnect(msg: Connect) {
        connections[msg.user] = msg.ws

        // TODO Bullshit stuff
        val questions = mutableListOf<QuestionEntry>()
        questions.add(QuestionEntry(0, "The first title."))
        questions.add(QuestionEntry(1, "The second title."))
        questions.add(QuestionEntry(2, "The third title."))
        questions.add(QuestionEntry(3, "The fourth title."))
        val response = QuestionsResponse(questions)
        val jsonContent = withContext(Dispatchers.IO) {
            jacksonObjectMapper().writeValueAsBytes(response)
        }
        msg.ws.send(jsonContent)
        // TODO End bullshit
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
            while (true) {
                for (frame in incoming) {
                    outgoing.send(Frame.Text("get fucked"))
                    result.input.send(frame)
                }
            }
        }
    }
}
