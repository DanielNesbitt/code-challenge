@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.genedata.ws

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.genedata.messages.Answer
import com.genedata.messages.RequestQuestions
import com.genedata.messages.generator.ReduxAction
import com.genedata.messages.generator.SocketAction
import com.genedata.models.*
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.withContext

/**
 * @author Daniel Nesbitt
 */
suspend fun DefaultWebSocketServerSession.createConnection(username:String) = actor<ReduxAction> {
    val user = getUser(username)
    if (user == null) {
        // close
    } else {
        val userId = user.id
        var current: AnswerSet? = null
        for (msg in channel) {
            // TODO Business logic
            when (msg) {
                is RequestQuestions -> {
                    current = getAnswerSet(userId, msg.questionId)
                    current?.let{channel.send(getQuestion(msg.questionId) as ReduxAction)}
                }
                is Answer -> println(msg.questionId)
            }
        }
    }
}

suspend fun DefaultWebSocketServerSession.connect(user: String, manager: SendChannel<ConnectionManagerMsg>) {
    manager.send(Connect(user, this))

    val connection = createConnection(user)
    for (msg in incoming) {
        when (msg) {
            is Frame.Text -> connection.send(fromJson(msg.readText()))
        }
    }
}

private suspend fun fromJson(value: String): SocketAction {
    return withContext(Dispatchers.IO) {
        jacksonObjectMapper()
            .readValue(value, SocketAction::class.java)
    }
}

