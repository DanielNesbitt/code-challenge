@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.genedata.ws

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.genedata.messages.*
import com.genedata.models.DB
import com.genedata.models.User
import com.genedata.models.getUser
import com.genedata.questions.Questions
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import io.ktor.websocket.WebSocketServerSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Daniel Nesbitt
 */
private val logger: Logger = LoggerFactory.getLogger("ConnectionHandler")

suspend fun WebSocketServerSession.connect(user: String, manager: SendChannel<ConnectionManagerMsg>) {
    manager.send(Connect(user, this))

    val connection = createConnection(user)
    for (msg in incoming) {
        try {
            when (msg) {
                is Frame.Text -> connection.send(fromJson(msg.readText()))
            }
        } catch (th: Throwable) {
            logger.error("Failed parse websocket frame into JSON.", th)
            connection.send(ErrorMessage("Failed to parse client input."))
        }
    }
}

private suspend fun WebSocketServerSession.createConnection(username: String) = actor<ReduxAction> {
    val user = getUser(username)
    if (user == null) {
        close(RuntimeException("No user for ws connection."))
    } else {
        with(ConnectionScope(user, this@createConnection)) {
            send(Questions.list(user))
            for (msg in channel) {
                when (msg) {
                    is RequestQuestion -> {
                        question(msg)
                    }
                    is Answer -> answer(msg)
                }
            }
        }
    }
}

private class ConnectionScope(val user: User, private val delegate: WebSocketServerSession) :
    WebSocketServerSession by delegate

private suspend fun ConnectionScope.question(msg: RequestQuestion) {
    val correctAnswer = DB.queryAnswer(user.id, msg.questionId)
    send(Questions.get(msg.questionId).toQuestion())
    if (correctAnswer != null) {
        send(correctAnswer)
    }
}

private suspend fun ConnectionScope.answer(action: Answer) {
    val result = try {
        DB.submitAnswer(action, user.id)
    } catch (th: Throwable) {
        logger.error("For question ${action.questionId} failed to submit answer ${action.answer}.", th)
        false
    }
    send(AnswerResult(action.questionId, action.answer, result))
    if (result) {
        send(Questions.list(user))
    }
}

private suspend fun ConnectionScope.send(action: ReduxAction) {
    send(Json.toJsonString(action))
}

private suspend fun fromJson(value: String): SocketAction {
    return withContext(Dispatchers.IO) {
        jacksonObjectMapper()
            .readValue(value, SocketAction::class.java)
    }
}
