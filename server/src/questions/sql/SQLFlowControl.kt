package com.genedata.questions.sql

import com.genedata.questions.Question

/**
 * @author Daniel Nesbitt
 */
class SQLFlowControl : Question {

    private val answerSet = listOf(
        "jack",
        "steven",
        "lucas",
        "sally",
        "beth",
        "yu",
        "minh",
        "jean"
    )

    override fun title(): String {
        return "SQL: Flow Control"
    }

    override fun text(): String {
        return """
            Given the following table Person:
            
            | id | name |
            | --- | --- |
            | 1 | steven |
            | 2 | jack |
            | 3 | sally |
            | 4 | lucas |
            | 5 | yu |
            | 6 | beth |
            | 7 | jean |
            | 8 | minh |

            
            What is the output of the name column of the following query?
            
            ```sql
            select
                (case
                    when mod(id, 2) != 0 and counts != id then id + 1
                    when mod(id, 2) != 0 and counts = id then id
                    else id - 1
                end) as id,
                name
            from
                person,
                (
                    select count(*) as counts
                    from person
                ) as seat_counts
            order by id;
            ```
            Separate values with commas.
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .withIndex()
            .all { it.index < answerSet.size && answerSet[it.index].equals(it.value, true) }
    }

}
