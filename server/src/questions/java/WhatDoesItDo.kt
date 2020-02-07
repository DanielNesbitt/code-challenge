package com.genedata.questions.java

import com.genedata.questions.Question

/**
 * @author Alice Li
 */
class WhatDoesItDo: Question {
    override fun title(): String = "Java: What Does This Do"

    override fun text(): String ="""
        ```java
        public final class Foo {

            private final boolean[][] a;
            private final int[][] b;
        
            public Foo(boolean[][] a) {
                this.a = a;
                this.b = new int[a.length][a.length];
                for (int[] bs : b) {
                    Arrays.fill(bs, -1);
                }
            }
        
            public final int foo(int x, int y) {
                if (b[x][y] != -1 || a[x][y]) {
                    return 0;
                } else {
                    return bar(x, y, new ArrayList<>(), 0);
                }
            }
        
            private final int bar(int x, int y, List<int[]> list, int z) {
                int n = 0;
                for (int i = Math.max(0, x - 1); i <= Math.min(a.length - 1, x + 1); i++) {
                    for (int j = Math.max(0, y - 1); j <= Math.min(a.length - 1, y + 1); j++) {
                        if (a[i][j]) {
                            n++;
                        }
                    }
                }
                b[x][y] = n;
                z++;
                if (b[x][y] == 0) {
                    for (int i = Math.max(0, x - 1); i <= Math.min(a.length - 1, x + 1); i++) {
                        for (int j = Math.max(0, y - 1); j <= Math.min(a.length - 1, y + 1); j++) {
                            if (b[i][j] == -1) {
                                list.add(new int[]{i, j});
                            }
                        }
                    }
                }
                if (!list.isEmpty()) {
                    int[] p = list.get(0);
                    list.remove(0);
                    return bar(p[0], p[1], list, z);
                }
                return z;
            }
        }
        ```
        ```java
        
        Foo foo = new Foo (new boolean[][] {
                {
                    false, false, true, true, false
                },
                {
                        false, false, false, true, false
                },
                {
                        true, false, true, true, false
                },
                {
                        false, false, false, false, false
                },
                {
                        false, false, false, true, true
                }
        });
        ```
        
        What is the result of foo.foo(4, 1)?
    """.trimIndent()

    override fun validateAnswer(answer: String): Boolean {
        return answer.trim().equals("6", false)
    }
}
