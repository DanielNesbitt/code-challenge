package com.genedata.messages

import com.genedata.messages.generator.ReduxAction
import com.genedata.messages.generator.SocketAction
import com.genedata.messages.generator.TSGenerator
import java.io.File


/**
 * @author Daniel Nesbitt
 */
data class RequestQuestions(
    val questionId: Long
) : ReduxAction

data class QuestionEntry(
    val questionId: Long,
    val questionTitle: String
)

data class QuestionsResponse(
    val questions: List<QuestionEntry>
) : ReduxAction

data class Question(
    val questionId: Long,
    val title: String,
    val text: String
) : ReduxAction

data class Answer(
    val questionId: Long,
    val answer: String
) : SocketAction

val RPC_CLASSES = setOf(
    RequestQuestions::class,
    QuestionsResponse::class,
    Question::class,
    Answer::class
)

fun main() {
    val rpcMessages = TSGenerator(
        RPC_CLASSES
    ).definitionsText

    val template = "// AUTO-GENERATED! Do not edit!\n${rpcMessages}"

    File("client/src/state/ServerRPC.ts").writeText(template)
    println(rpcMessages)
}
