package com.genedata.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Alice Li
 */

object Groups : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
    val password = varchar("password", 50)
}

data class Group(val id: Int, val name: String, val password: String)

object Questions: Table() {
    val id = uuid("id")
    val text = varchar("text", 500)
}

object Scores: Table() {
    val group = uuid("groupId")
    val problem = uuid("problemId")
    val score = integer("score")
}

class DB {
    init {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Groups, Questions, Scores)
        }
    }

    fun newGroup(newGroupName: String, newGroupPwd: String): String {
        return transaction {
            if (Groups.slice(Groups.name).selectAll().map{ it[Groups.name] }.toSet().contains(newGroupName)) {
                throw RuntimeException("Group name ${newGroupName} already in use.")
            }
            Groups.insert {
                it[name] = newGroupName
                it[password] = newGroupPwd
            } get Groups.name
        }
    }

    fun getGroup(name: String): Group? {
        return transaction {
            Groups.select { Groups.name eq name }.map {
                Group(
                    id = it[Groups.id],
                    name = it[Groups.name],
                    password = it[Groups.password]
                )
            }.firstOrNull()
        }
    }
}
