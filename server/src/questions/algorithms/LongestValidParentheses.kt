package com.genedata.questions.algorithms

import com.genedata.questions.NeedsWorks
import com.genedata.questions.Question

/**
 * @author Daniel Nesbitt
 */
class LongestValidParentheses: Question {

    override fun title(): String {
        return "Algorithms: Dynamic Programming"
    }

    override fun text(): String {
        return """
            You might have an easier time with this problem if you determine the intent of the algorithm before trying 
            to solve it based on the input.
            ```java
            public int findX(String s) {
                int maxans = 0;
                int dp[] = new int[s.length()];
                for (int i = 1; i < s.length(); i++) {
                    if (s.charAt(i) == ')') {
                        if (s.charAt(i - 1) == '(') {
                            dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                        } else if (i - dp[i - 1] > 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
                            dp[i] = dp[i - 1] + ((i - dp[i - 1]) >= 2 ? dp[i - dp[i - 1] - 2] : 0) + 2;
                        }
                        maxans = Math.max(maxans, dp[i]);
                    }
                }
                return maxans;
            }
            ```
            ```java
            int x = findX(")()())))())((((()(())))()((())))((()()()");
            ```
            What is x?
            ```
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.toInt() == 20;
    }

}
