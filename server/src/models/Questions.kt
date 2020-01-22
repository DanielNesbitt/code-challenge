package com.genedata.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Alice Li
 */

object Questions: Table() {
    val id = Questions.integer("question_id").autoIncrement().primaryKey().uniqueIndex()
    val code = Questions.varchar("code", 500)
}

data class Question(val id: Int, val code: String)

object QuestionsAndAnswers: Table() {
    val id = QuestionsAndAnswers.integer("qa_id").autoIncrement().primaryKey().uniqueIndex()
    val question = QuestionsAndAnswers.integer("question_id").uniqueIndex()
    val input = QuestionsAndAnswers.varchar("input", 500)
    val output = QuestionsAndAnswers.varchar("output", 500)
}

data class QandA(val id: Int, val question: Int, val input: String, val output: String)

fun getQuestion(group: String): String? {
    return transaction {
        val lastQuestion = (Groups innerJoin Scores)
            .slice(Groups.name, Scores.question)
            .select { Groups.name eq group}
            .map {it[Scores.question]}
            .max()

        val next = lastQuestion?.let{ it + 1 } ?: 0
        Questions.select { Questions.id eq next }
            .map { it[Questions.code] }
            .firstOrNull()
    }
}
