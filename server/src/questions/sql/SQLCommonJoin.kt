package com.genedata.questions.sql

import com.genedata.questions.Question

/**
 * @author Daniel Nesbitt
 */
class SQLCommonJoin: Question {
    override fun title(): String {
        return "SQL: Common Join"
    }

    override fun text(): String {
        return """
            Given the following tables:
            
            **Salary**
            
            |employee_id|salary|
            |---|---|
            | 1 | 3 |
            | 2 | 10 |
            | 3 | 10 |
            | 4 | 8 |
            | 5 | 3 |
            | 6 | 7 |
            | 7 | 9 |
            | 8 | 9 |
            
            **Department**
            
            |id|name|
            |---|---|
            |1|IT|
            |2|Sales|

            **DepartmentAssignments**
            
            |employee_id|department_id|
            |---|---|
            |1|1|
            |2|1|
            |3|2|
            |4|2|
            |5|2|
            |6|1|
            |7|2|
            |8|1|
            
            The Employee table is omitted for brevity.
            
            ```sql
            select sum(s.salary)
            from department dep
            join departmentassignment da on dep.id = da.department_id
            join salary s on da.employee_id = s.employee_id
            where department = 'IT'
            group by dep.id
            ```
            
            What is the output of this query?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.toInt() == 29
    }
}
