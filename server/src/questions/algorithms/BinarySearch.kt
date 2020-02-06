package com.genedata.questions.algorithms

import com.genedata.questions.NeedsWorks
import com.genedata.questions.Question

/**
 * @author Alice Li
 */
@NeedsWorks
class BinarySearch: Question {
    override fun title(): String {
        return "Algorithms: Very Easy"
    }

    override fun text(): String {
        return """
            ```java
            static int foo(int[] a, int b) {
                int c = 0;
                int d = a.length - 1;
                while (c <= d) {
                    int e = (c + d) /2;
                    if (a[e] < b) {
                        c = e + 1;
                    } else if (a[e] > b) {
                        d = e - 1;
                    } else {
                        return e;
                    }
                }
                return -1;
            }
            ```
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
