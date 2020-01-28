@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.genedata.ws

import com.genedata.messages.RequestQuestions
import com.genedata.messages.generator.ReduxAction
import com.genedata.models.Questions
import com.genedata.models.getUser
import io.ktor.http.cio.websocket.Frame
import io.ktor.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor

/**
 * @author Daniel Nesbitt
 */
suspend fun DefaultWebSocketServerSession.createConnection(username:String) = actor<ReduxAction> {
    val user = getUser(username)
    if (user == null) {
        // close
    } else {
        val userId = user.id
        for (msg in channel) {
            // TODO Business logic
        }
    }
}

suspend fun DefaultWebSocketServerSession.connect(user: String, manager: SendChannel<ConnectionManagerMsg>) {
    manager.send(Connect(user, this))

    val connection = createConnection(user)
    for (msg in incoming) {
        when (msg) {
            is Frame.Text -> connection.send(msg as ReduxAction) // TODO handle conversion
        }
    }
}
