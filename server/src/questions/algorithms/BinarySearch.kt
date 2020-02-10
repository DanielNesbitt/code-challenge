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
        int[] a = new int[] { 1, 3, 6, 13, 43, 55, 57, 87 };
        ```
        What is the result of foo(a, 6)?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer === "6";
    }

}
