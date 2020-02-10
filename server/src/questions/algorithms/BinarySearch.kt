package com.genedata.questions.algorithms

import com.genedata.questions.NeedsWorks
import com.genedata.questions.Question

/**
 * @author Alice Li
 */
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
        ```java
        int[] a = new int[] { 3, 13, 40, 52, 58, 87, 107, 130, 146, 151, 162, 176, 179, 206, 223, 266, 266, 273, 316, 342, 376, 377, 382, 387, 391, 448, 469, 474, 483, 492, 500, 529, 545, 554, 561, 562, 570, 587, 591, 598 };
        ```
        What is the result of foo(a, 391)?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.trim().toInt() == 24;
    }

}
