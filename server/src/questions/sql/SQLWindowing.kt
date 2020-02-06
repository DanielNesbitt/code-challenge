package com.genedata.questions.sql

import com.genedata.questions.Question

/**
 * @author Daniel Nesbitt
 */
class SQLWindowing: Question {
    private val answerSet = setOf(1, 2, 5)

    override fun title(): String {
        return "SQL: Windows"
    }

    override fun text(): String {
        return """
            Given the following table Numbers:
            
            |id | num|
            | --- | --- |
            |1 | 1|
            |2 | 1|
            |3 | 1|
            |4 | 3|
            |5 | 2|
            |6 | 2|
            |7 | 1|
            |8 | 1|
            |9 | 4|
            |10 | 4|
            |11 | 2|
            |12 | 2|
            |13 | 2|
            |14 | 2|
            |15 | 1|
            |16 | 3|
            |17 | 5|
            |18 | 5|
            |19 | 5|
            |20 | 2|
            |21 | 1|
            
            What is the output of following query (Postgres)?
            ```sql
            select distinct num
            from (
                select
                    num,
                    LEAD(num) over(order by id) as lead,
                    LAG(num) over (order by id) as lag
                from Numbers
            ) num_window
            where num=lead and num=lag;
            ```
            
            Answers should be parsable as ints. If multiple values are returned, separate them with commas.
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        val elements = answer.split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .toMutableSet()
        var correct = true
        for (i in answerSet) {
            correct = correct && elements.remove(i)
        }
        correct = correct && elements.isEmpty()
        return correct
    }
}
