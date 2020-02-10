package com.genedata.questions.java

import com.genedata.questions.Question

/**
 * @author Alice Li
 */
class BitManipulation: Question {
    override fun title(): String {
        return "Bit Manipulation"
    }

    override fun text(): String {
        return """
            ```java
            public static int foo(int a, int b) {
                return ~(a << 1 ^ (~b | b >>>1)); 
            }
            ```
            What is the result of foo(7, 8)?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.toInt() == 6;
    }

}
