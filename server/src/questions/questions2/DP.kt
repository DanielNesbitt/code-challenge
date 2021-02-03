package com.genedata.questions.questions2

import com.genedata.questions.Question

/**
 * @author Alice Li
 */
class DP : Question {
    override fun title(): String {
        return "Dynamic Programming"
    }

    override fun text(): String {
        return """
            ```java
            public static int foo(int[] nums) {
                int[] dp = new int[nums.length];
                int len = 0;
                for (int num : nums) {
                    int i = Arrays.binarySearch(dp, 0, len, num);
                    if (i < 0) {
                        i = -(i + 1);
                    }
                    dp[i] = num;
                    if (i == len) {
                        len++;
                    }
                }
                return len;
            }
            ```
            What is the result of foo(new int[] { 57, 1, 4, 9, 2, 5, 4, 3, 6, 1, 3, 3, 8, 7, 7 })?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.toInt() == 5;
    }
}
