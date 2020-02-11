package com.genedata.models

import com.genedata.messages.Answer
import com.genedata.messages.AnswerResult
import com.genedata.questions.Questions
import com.genedata.session.hashPassword
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

/**
 * @author Alice Li
 */

object Users : Table() {
    val id = long("user_id").autoIncrement().primaryKey().uniqueIndex()
    val name = varchar("name", 50)
    val passwordHash = varchar("passwordHash", 120)
    val admin = bool("admin").default(false)
}

object Answers : Table() {
    val id = long("answer_id").autoIncrement().primaryKey().uniqueIndex()
    val groupId = (long("group_id") references Users.id)
    val questionId = long("question_id")
    val text = varchar("answer", 2000)
    val correct = bool("correct")
}

data class User(val id: Long, val name: String, val passwordHash: String, val isAdmin: Boolean) {
    fun validatePassword(password: String): Boolean {
        return BCrypt.checkpw(password, passwordHash)
    }
}

data class UserAnswer(val name: String, val question: String, val answer: String, val correct: Boolean)

object DB {

    fun initialize() {
        val databaseUrl = System.getenv("DATABASE_URL")

        if (databaseUrl != null) {
            val databaseUser = System.getenv("DATABASE_USER")
            val databasePassword = System.getenv("DATABASE_PASSWORD")
            Database.connect(databaseUrl, user = databaseUser, password = databasePassword, driver = "org.postgresql.Driver")
        } else {
            Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        }
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Users, Answers)
        }
    }

    fun queryAnswers(group: Long): Map<Long, Boolean> {
        return transaction {
            Answers.select { Answers.groupId eq group and Answers.correct.eq(true) }.toList()
                .map { it[Answers.questionId] to it[Answers.correct] }.toMap()
        }
    }

    fun queryAnswer(group: Long, question: Long): AnswerResult? {
        return transaction {
            Answers.select { Answers.groupId eq group and Answers.correct.eq(true) and Answers.questionId.eq(question) }
                .limit(1)
                .toList()
                .map { AnswerResult(question, it[Answers.text], it[Answers.correct]) }
                .singleOrNull()
        }
    }

    fun submitAnswer(ans: Answer, group: Long): Boolean {
        return transaction {
            Answers.insert {
                it[groupId] = group
                it[questionId] = ans.questionId
                it[text] = ans.answer
                it[correct] = Questions.validate(ans.questionId, ans.answer)
            } get Answers.correct
        }
    }

}

fun newUser(newUserName: String, newUserPwd: String, isAdmin: Boolean = false): String {
    return transaction {
        if (Users.slice(Users.name).selectAll().map { it[Users.name] }.toSet().contains(newUserName)) {
            throw RuntimeException("User name $newUserName already in use.")
        }
        val pass = hashPassword(newUserPwd)
        Users.insert {
            it[name] = newUserName
            it[passwordHash] = pass
            it[admin] = isAdmin
        } get Users.name
    }
}

fun getUser(name: String): User? {
    return transaction {
        Users.select { Users.name eq name }.map {
            User(
                id = it[Users.id],
                name = it[Users.name],
                passwordHash = it[Users.passwordHash],
                isAdmin = it[Users.admin]
            )
        }.firstOrNull()
    }
}

fun listUsersAndAnswers(): List<UserAnswer> {
    return transaction {
        Answers.join(Users, JoinType.INNER)
            .slice(Users.name, Answers.questionId, Answers.text, Answers.correct )
            .selectAll()
            .orderBy(Users.name to SortOrder.ASC, Answers.questionId to SortOrder.ASC, Answers.correct to SortOrder.ASC)
            .map {
                UserAnswer(
                    it[Users.name],
                    Questions.get(it[Answers.questionId]).title(),
                    it[Answers.text],
                    it[Answers.correct]
                )
            }
    }
}