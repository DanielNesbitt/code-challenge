package com.genedata.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * @author Alice Li
 */

object Groups : Table() {
    val id = uuid("id")
    val name = varchar("name", 50)
    val password = varchar("password", 50)
}

object Questions: Table() {
    val id = uuid("id")
    val text = varchar("text", 500)
}

object Scores: Table() {
    val group = uuid("groupId")
    val problem = uuid("problemId")
    val score = integer("score")
}

class Foo {
    init {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Groups, Questions, Scores)
        }
    }

    fun newGroup(n: String, p: String) {
        transaction {
            val f = Groups.selectAll()
            println(f.count())
            val newGroupId = Groups.insert {
                it[id] = UUID.randomUUID()
                it[name] = n
                it[password] = p
            } get Groups.id

            println("All groups:")
            val groups = Groups.selectAll()
            println(groups.count())
            for (group in groups) {
                println("${group[Groups.name]}: ${group[Groups.id]}")
            }
        }
    }
}
