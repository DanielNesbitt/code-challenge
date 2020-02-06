package com.genedata.questions.java

import com.genedata.questions.Question

/**
 * @author Daniel Nesbitt
 */
class JavaStreams: Question {
    override fun title(): String = "Java: Streams"

    override fun text(): String ="""
        ```java
        public class Streaming {
            String find_x(List<String> input) {
                return input.stream()
                        .map(s -> s + s.substring(1, 3))
                        .filter(this::isPallindrome)
                        .sorted()
                        .findFirst()
                        .get();
        
            }
        
            boolean isPallindrome(String s) {
                String reversed = new StringBuilder(s)
                        .reverse()
                        .toString();
                return s.equals(reversed);
            }
        }        
        ```
        ```java
        List<String> ss = List.of( "akk", "cuc", "aap", "aak", "cac", "ded" );
        ```
        
        What is the result of `find_x` with the input `ss`?
    """.trimIndent()

    override fun validateAnswer(answer: String): Boolean {
        return answer.trim().equals("cacac", false)
    }
}
