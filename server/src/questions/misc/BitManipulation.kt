package com.genedata.questions.misc

import com.genedata.questions.NeedsWorks
import com.genedata.questions.Question

/**
 * @author Alice Li
 */
class BitManipulation: Question {
    override fun title(): String {
        return "BitManipulation"
    }

    override fun text(): String {
        return """
            ```java
            public static int foo(int a, int b) {
                return ~(a << 1 ^ (~b | b >>>1)) 
            }
            ```
            What is the result of foo(7, 8)?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer === "6";
    }

    fun foo(a: Int, b: Int): Int {
        return (a shl 1 xor (b.inv() or b ushr 1)).inv()
    }

}
