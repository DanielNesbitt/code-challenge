package com.genedata.models

import com.genedata.messages.Answer
import com.genedata.messages.Question
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

object Answers : Table() {
    val id = Answers.long("answer_id").autoIncrement().primaryKey().uniqueIndex()
    val question = Answers.long("question_id").uniqueIndex()
    val input = Answers.varchar("input", 500)
    val output = Answers.varchar("output", 500)
}

data class AnswerSet(val id: Long, val question: Long, val input: String, val output: String)

object Scores : Table() {
    val user = long("user_id").uniqueIndex()
    val answer = long("answer_id").uniqueIndex()
    val score = integer("score")
}

fun getAnswerSet(userId: Long, question: Long): AnswerSet? {
    return transaction {
        val previousAnswers = Scores.slice(Scores.answer)
            .select { Scores.user eq userId }
            .map { it[Scores.answer] }
        val availableAnswers = Answers.select { Answers.question eq question }
            .andWhere { Answers.id notInList previousAnswers }
            .map { AnswerSet(it[Answers.id], it[Answers.question], it[Answers.input], it[Answers.output]) }
        if (availableAnswers.isNotEmpty()) availableAnswers[Random.nextInt(0, availableAnswers.size)] else null
    }
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

fun getQuestion(questionId: Long): Question? {
    return transaction {
        Questions.select { Questions.id eq questionId }
            .map { Question(it[Questions.id], it[Questions.title], it[Questions.code]) }
            .firstOrNull()
    }
}

fun listQuestions(): QuestionsResponse {
    return transaction {
        QuestionsResponse(Questions.selectAll()
            .map{QuestionEntry(it[Questions.id], it[Questions.title])})

    }
}
