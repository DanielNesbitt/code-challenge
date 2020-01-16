package com.genedata.messages

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.genedata.messages.generator.ReduxAction
import com.genedata.messages.generator.TSGenerator
import java.io.File


/**
 * @author Daniel Nesbitt
 */
@ReduxAction
data class RequestQuestions(
    val questionId: Long
)

data class QuestionEntry(
    val questionId: Long,
    val questionTitle: String
)

@ReduxAction
data class QuestionsResponse(
    val questions: List<QuestionEntry>
)

@ReduxAction
data class Answer(
    val questionId: Long,
    val answer: String
)

fun main() {
    val value = jacksonObjectMapper().valueToTree<ObjectNode>(RequestQuestions(1))
    println(value.toString())


    val rpcMessages = TSGenerator(
        setOf(
            RequestQuestions::class,
            QuestionsResponse::class,
            Answer::class
        )
    ).definitionsText

    val template = "// AUTO-GENERATED! Do not edit!\nimport {Action} from \"redux\";\n\n${rpcMessages}"

    File("client/src/state/ServerRPC.ts").writeText(template)
    println(rpcMessages)
}
