package com.genedata.models

import com.genedata.session.hashPassword
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

/**
 * @author Alice Li
 */

object Groups : Table() {
    val id = integer("group_id").autoIncrement().primaryKey().uniqueIndex()
    val name = varchar("name", 50)
    val passwordHash = varchar("passwordHash", 120)
}

data class Group(val id: Int, val name: String, val passwordHash: String) {
    fun validatePassword(password: String): Boolean {
        return BCrypt.checkpw(password, passwordHash)
    }
}

object Scores: Table() {
    val group = integer("group_id").uniqueIndex()
    val question = integer("question_id").uniqueIndex()
    val score = integer("score")
}

class DB {
    init {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Groups, Questions, QuestionsAndAnswers, Scores)
        }
    }

    // Groups

    fun newGroup(newGroupName: String, newGroupPwd: String): String {
        return transaction {
            if (Groups.slice(Groups.name).selectAll().map{ it[Groups.name] }.toSet().contains(newGroupName)) {
                throw RuntimeException("Group name ${newGroupName} already in use.")
            }
            val pass = hashPassword(newGroupPwd)
            Groups.insert {
                it[name] = newGroupName
                it[passwordHash] = pass
            } get Groups.name
        }
    }

    fun getGroup(name: String): Group? {
        return transaction {
            Groups.select { Groups.name eq name }.map {
                Group(
                    id = it[Groups.id],
                    name = it[Groups.name],
                    passwordHash = it[Groups.passwordHash]
                )
            }.firstOrNull()
        }
    }

}
