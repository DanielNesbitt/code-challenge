package com.genedata.questions.misc

import com.genedata.questions.Question

/**
 * @author Daniel Nesbitt
 */
class HaskellFib : Question {

    override fun title(): String = "Haskell: Lazy Lists"

    override fun text(): String = """
        ```haskhell
        ll = 0 : 1 : zipWith (+) ll (tail ll)
        print ${'$'} ll!!9
        ```
        What does this print?
    """.trimIndent()

    override fun validateAnswer(answer: String): Boolean = answer.toInt() == 34
}
