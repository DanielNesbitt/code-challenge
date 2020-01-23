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
    val code: String,
    val input: String
) : ReduxAction

data class Answer(
    val questionId: Long,
    val answer: String
) : SocketAction

fun main() {
    val rpcMessages = TSGenerator(
        setOf(
            RequestQuestions::class,
            QuestionsResponse::class,
            Answer::class,
            Question::class
        )
    ).definitionsText

    val template = "// AUTO-GENERATED! Do not edit!\nimport {Action} from \"redux\";\n\n${rpcMessages}"

    File("client/src/state/ServerRPC.ts").writeText(template)
    println(rpcMessages)
}
