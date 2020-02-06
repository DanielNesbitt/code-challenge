package com.genedata.questions.misc

import com.genedata.questions.NeedsWorks
import com.genedata.questions.Question

/**
 * @author Alice Li
 */
@NeedsWorks
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
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun foo(a: Int, b: Int): Int {
        return (a shl 1 xor (b.inv() or b ushr 1)).inv()
    }

}
