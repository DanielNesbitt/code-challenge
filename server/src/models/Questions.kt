package com.genedata.models

import com.genedata.messages.QuestionEntry
import com.genedata.messages.QuestionsResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.Random

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

data class Question(val id: Int, val title: String, val code: String)

object Answers : Table() {
    val id = Answers.long("answer_id").autoIncrement().primaryKey().uniqueIndex()
    val question = Answers.long("question_id").uniqueIndex()
    val input = Answers.varchar("input", 500)
    val output = Answers.varchar("output", 500)
}

data class Answer(val id: Int, val question: Int, val input: String, val output: String)

object Scores : Table() {
    val user = long("user_id").uniqueIndex()
    val answer = long("answer_id").uniqueIndex()
    val score = long("score")
}

fun getAnswer(userId: Int, question: Int): Answer {
    return transaction {
        val previousAnswers = Scores.slice(Scores.answer)
            .select { Scores.user eq userId }
            .map { it[Scores.answer] }
        val availableAnswers = Answers.select { Answers.question eq question }
            .andWhere { Answers.id notInList previousAnswers }
            .map { Answer(it[Answers.id], it[Answers.question], it[Answers.input], it[Answers.output]) }
        availableAnswers[Random.nextInt(0, availableAnswers.size)]
    }
}

fun submitAnswer(userId: Int, submitted: String, current: Answer) {
    transaction {
        Scores.insert {
            it[user] = userId
            it[answer] = current.id
            it[score] = if (current.output == submitted) CORRECT_POINTS else INCORRECT_POINTS
        }
    }
}

fun getQuestion(userId: Int): String? {
    return transaction {
        val lastQuestion = Scores.select { Scores.user eq userId }
            .map { it[Scores.answer] }
            .max()

        val next = lastQuestion?.let { it + 1 } ?: 0
        Questions.select { Questions.id eq next }
            .map { it[Questions.code] }
            .firstOrNull()
    }
}

fun listQuestions(): QuestionsResponse {
    return transaction {
        QuestionsResponse(Questions.selectAll()
            .map{QuestionEntry(it[Questions.id], it[Questions.title])})

    }
}
