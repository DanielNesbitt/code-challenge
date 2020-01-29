@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.genedata.ws

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.genedata.messages.*
import com.genedata.models.AnswerSet
import com.genedata.models.getAnswerSet
import com.genedata.models.getQuestion
import com.genedata.models.getUser
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import io.ktor.websocket.WebSocketServerSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.withContext

/**
 * @author Daniel Nesbitt
 */
suspend fun WebSocketServerSession.createConnection(username: String) = actor<ReduxAction> {
    val user = getUser(username)
    if (user == null) {
        // close
    } else {
        send(dummyQuestions())
        val userId = user.id
        var current: AnswerSet? = null
        for (msg in channel) {
            // TODO Business logic
            when (msg) {
                is RequestQuestions -> {
                    current = getAnswerSet(userId, msg.questionId)
                    current?.let { channel.send(getQuestion(msg.questionId) as ReduxAction) }
                }
                is Answer -> println(msg.questionId)
            }
        }
    }
}

suspend fun WebSocketServerSession.connect(user: String, manager: SendChannel<ConnectionManagerMsg>) {
    manager.send(Connect(user, this))

    val connection = createConnection(user)
    for (msg in incoming) {
        try {
            when (msg) {
                is Frame.Text -> connection.send(fromJson(msg.readText()))
            }
        } catch (th: Throwable) {
            th.printStackTrace()
        }
    }
}

suspend fun WebSocketServerSession.send(action: ReduxAction) {
    send(Json.toJsonString(action))
}

private suspend fun fromJson(value: String): SocketAction {
    return withContext(Dispatchers.IO) {
        jacksonObjectMapper()
            .readValue(value, SocketAction::class.java)
    }
}

private fun dummyQuestions() = QuestionsResponse(listOf(
    QuestionEntry(0, "The first question"),
    QuestionEntry(1, "The second question"),
    QuestionEntry(2, "The third question")
))

