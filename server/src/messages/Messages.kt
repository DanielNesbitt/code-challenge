package com.genedata.messages

import com.genedata.messages.generator.ReduxAction
import com.genedata.messages.generator.TSGenerator
import java.io.File


/**
 * @author Daniel Nesbitt
 */
@ReduxAction
data class RequestQuestion(
    val questionId: Long
)

@ReduxAction
data class Question(
    val questionId: Long,
    val question: String
)

@ReduxAction
data class Answer(
    val questionId: Long,
    val answer: String,
    val options: Map<String, String>
)

fun main() {
    val rpcMessages = TSGenerator(
        setOf(
            RequestQuestion::class,
            Question::class,
            Answer::class
        )
    ).definitionsText

    val template = "// AUTO-GENERATED! Do not edit!\n${rpcMessages}"

    File("client/src/state/ServerRPC.ts").writeText(template)
    println(rpcMessages)
}
