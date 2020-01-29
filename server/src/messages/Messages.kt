package com.genedata.messages

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.genedata.messages.generator.TSGenerator
import java.io.File


/**
 * @author Daniel Nesbitt
 */
fun main() {
    val rpcMessages = TSGenerator(
        RPC_CLASSES
    ).definitionsText

    val template = "// AUTO-GENERATED! Do not edit!\n${rpcMessages}"

    File("client/src/state/ServerRPC.ts").writeText(template)
    println(rpcMessages)
}

// ===============================================

/** Defines the base type for actions. Direct usages of this class usually confer client -> server messages. */
interface ReduxAction {
    @JsonSerialize
    fun type(): String {
        return this::class.simpleName!!
    }
}

/**
 * Defines the type for actions sent from client to server. All possible extensions must be annotated here so
 * that Jackson understands how to resolve the specific class from the action type during deserialization. The
 * type info is excluded as it provided by the parent class.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(Answer::class, name = "Answer")
)
@JsonIgnoreProperties(ignoreUnknown = true)
interface SocketAction : ReduxAction

// ===============================================

data class RequestQuestions(
    val questionId: Long
) : ReduxAction

data class QuestionEntry(
    val questionId: Long,
    val title: String
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

data class AnswerResult(
    val questionId: Long,
    val correct: Boolean
) : ReduxAction

// ===============================================

val RPC_CLASSES = setOf(
    RequestQuestions::class,
    QuestionsResponse::class,
    Question::class,
    Answer::class,
    AnswerResult::class
)
