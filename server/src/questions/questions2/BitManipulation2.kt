package com.genedata.questions.questions2

import com.genedata.questions.Question

/**
 * @author Alice Li
 */
class BitManipulation2 : Question {
    override fun title(): String {
        return "Java"
    }

    override fun text(): String {
        return """
            ```java
            public static int foo(int a, int b) {
                ~(~a >> 2 & b) ^ (b << 1 | a);
            }
            ```
            What is the result of foo(5, 9)?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.toInt() == -32;
    }
}
