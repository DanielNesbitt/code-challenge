package com.genedata.questions.questions2

import com.genedata.questions.Question

/**
 * @author Daniel Nesbitt
 */
class SQL: Question {
    override fun title(): String {
        return "SQL"
    }

    override fun text(): String {
        return """
            Given the following tables:
            
            **Salary**
            
            |student_id|gpa|
            |---|---|
            | 1 | 3.5 |
            | 2 | 4.2 |
            | 3 | 3.8 |
            | 4 | 4.0 |
            | 5 | 4.5 |
            | 6 | 2.5 |
            | 7 | 3.2 |
            | 8 | 3.6 |
            
            **Courses**
            
            |id|name|
            |---|---|
            |1|Civil and Environmental Engineering|
            |2|Mechanical Engineering|
            |3|Materials Science and Engineering|
            |4|Architecture|
            |5|Chemistry|
            |6|Electrical Engineering and Computer Science|

            **CourseAssignments**
            
            |studend_id|course_id|
            |---|---|
            |1|3|
            |2|1|
            |3|6|
            |4|6|
            |5|4|
            |6|5|
            |7|2|
            |8|6|
            
            ```sql
            select avg(s.gpa)
            from courses c
            join courseassignments ca on c.id = c.course_id
            join gpa g on ca.student_id = s.student_id
            where department = 'Electrical Engineering and Computer Science'
            ```
            
            What is the output of this query?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.toDouble() == 3.8
    }
}
