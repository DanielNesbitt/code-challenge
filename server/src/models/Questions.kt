package com.genedata.models

import com.genedata.messages.Answer
import com.genedata.messages.QuestionEntry
import com.genedata.messages.QuestionsResponse
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Alice Li
 */

val CORRECT_POINTS = 5;
val INCORRECT_POINTS = -1;

object Questions : Table() {
    val id = Questions.long("question_id").autoIncrement().primaryKey().uniqueIndex()
    val title = Questions.varchar("title", 50)
    val code = Questions.varchar("code", 500)
}

data class AnswerSet(val id: Long, val question: Long, val input: String, val output: String)

object Scores : Table() {
    val user = long("user_id").uniqueIndex()
    val answer = long("answer_id").uniqueIndex()
    val score = integer("score")
}

fun submitAnswer(userId: Long, submitted: Answer, current: AnswerSet) {
    return transaction {
        if (current.question == submitted.questionId) {
            Scores.insert {
                it[user] = userId
                it[answer] = current.id
                it[score] = if (current.output == submitted.answer) CORRECT_POINTS else INCORRECT_POINTS
            }
        }
    }
}


fun listQuestions(): QuestionsResponse {
    return transaction {
        QuestionsResponse(Questions.selectAll()
            .map{QuestionEntry(it[Questions.id], it[Questions.title])})

    }
}
