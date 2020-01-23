package com.genedata.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.Random

/**
 * @author Alice Li
 */

val CORRECT_POINTS = 5;
val INCORRECT_POINTS = -1;

object Questions: Table() {
    val id = Questions.integer("question_id").autoIncrement().primaryKey().uniqueIndex()
    val code = Questions.varchar("code", 500)
}

data class Question(val id: Int, val code: String)

object Answers: Table() {
    val id = Answers.integer("answer_id").autoIncrement().primaryKey().uniqueIndex()
    val question = Answers.integer("question_id").uniqueIndex()
    val input = Answers.varchar("input", 500)
    val output = Answers.varchar("output", 500)
}

data class Answer(val id: Int, val question: Int, val input: String, val output: String)

object Scores: Table() {
    val user = integer("user_id").uniqueIndex()
    val answer = integer("answer_id").uniqueIndex()
    val score = integer("score")
}

fun getAnswer(user: String, question: Int): Answer {
    return transaction {
        val previousAnswers = (Users innerJoin Scores)
            .slice(Scores.answer)
            .select { Users.name eq user }
            .map { it[Scores.answer]}
        val availableAnswers = Answers.select { Answers.question eq question }
            .andWhere { Answers.id notInList previousAnswers }
            .map{ Answer(it[Answers.id], it[Answers.question], it[Answers.input], it[Answers.output])}
        availableAnswers[Random.nextInt(0, availableAnswers.size)]
    }
}

fun submitAnswer(user: String, submitted: String, current: Answer) {
    transaction {
        val userId = Users.select { Users.name eq user }
            .map{ it[Users.id]}
            .firstOrNull()
        if (userId != null) {
            Scores.insert {
                it[Scores.user] = userId
                it[answer] = current.id
                it[score] = if (current.output == submitted) CORRECT_POINTS else INCORRECT_POINTS
            }
        }
    }
}

fun getQuestion(user: String): String? {
    return transaction {
        val lastQuestion = (Users innerJoin Scores)
            .slice(Users.name, Scores.answer)
            .select { Users.name eq user}
            .map {it[Scores.answer]}
            .max()

        val next = lastQuestion?.let{ it + 1 } ?: 0
        Questions.select { Questions.id eq next }
            .map { it[Questions.code] }
            .firstOrNull()
    }
}
