package com.genedata.questions.questions2

import com.genedata.questions.Question

/**
 * @author Alice Li
 */
class Heapsort : Question {
    override fun title(): String {
        return "Java"
    }

    override fun text(): String {
        return """
            ```java
            public static final void foo(int[] a) {
                bar(a);
                for (int i = a.length - 1; i > 0; i--) {
                    int foo = a[0];
                    a[0] = a[i];
                    a[i] = foo;
                    baz(a, i, 0);
                }
            }
        
            public static final void bar(int[] a) {
                for(int i = a.length / 2; i >= 0; i--) {
                    baz(a, a.length, i);
                }
            }
        
            public static final void baz(int[] a, int b, int c) {
                int d = c << 1 + 1;
                int e = c << 1 + 2;
                int f;
                if (d < b && a[d] > a[c]) {
                    f = d;
                } else {
                    f = c;
                }
                if (e < b && a[e] > a[f]) {
                    f = e;
                }
                if (f != c) {
                    int g = a[c];
                    a[c] = a[f];
                    a[f] = g;
                    baz(a, b, f);
                }
            }
                    
            int[] a = {7, 55, 33, 88, 4, 79, 94, 69, 86, 31, 98, 5, 43, 49, 87};            
            ```
            What is the result of foo(a)?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.toInt() == 55;
    }
}
