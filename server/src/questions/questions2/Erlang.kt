package com.genedata.questions.questions2

import com.genedata.questions.Question

/**
 * @author Alice Li
 */
class Erlang : Question {
    override fun title(): String {
        return "Erlang"
    }

    override fun text(): String {
        return """
            ```erlang
            foo([]) -> [].
            foo([P|R]) -> foo([F || F <- R, F < P]) ++ [P] ++ foo([B || B <- R, B >= P]).
            ```
           What is foo([2,1,5])?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.trim() == "[1,2,5]" || answer.trim() == "[1, 2, 5]";
    }
}
