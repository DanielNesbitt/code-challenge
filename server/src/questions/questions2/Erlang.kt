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
            foo([]) -> [];
            foo([P|R]) -> foo([F || F <- R, F < P]) ++ [P] ++ foo([B || B <- R, B >= P]).
            ```
           What is this?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.trim().toLowerCase() == "quicksort"
    }
}
