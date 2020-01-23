package com.genedata.models

import com.genedata.session.hashPassword
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

/**
 * @author Alice Li
 */

object Users : Table() {
    val id = integer("user_id").autoIncrement().primaryKey().uniqueIndex()
    val name = varchar("name", 50)
    val passwordHash = varchar("passwordHash", 120)
}

data class User(val id: Int, val name: String, val passwordHash: String) {
    fun validatePassword(password: String): Boolean {
        return BCrypt.checkpw(password, passwordHash)
    }
}

class DB {
    init {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Users, Questions, Answers, Scores)
        }
    }

    fun newUser(newUserName: String, newUserPwd: String): String {
        return transaction {
            if (Users.slice(Users.name).selectAll().map{ it[Users.name] }.toSet().contains(newUserName)) {
                throw RuntimeException("User name ${newUserName} already in use.")
            }
            val pass = hashPassword(newUserPwd)
            Users.insert {
                it[name] = newUserName
                it[passwordHash] = pass
            } get Users.name
        }
    }

    fun getUser(name: String): User? {
        return transaction {
            Users.select { Users.name eq name }.map {
                User(
                    id = it[Users.id],
                    name = it[Users.name],
                    passwordHash = it[Users.passwordHash]
                )
            }.firstOrNull()
        }
    }

}
