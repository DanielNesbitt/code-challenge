package com.genedata.questions

import kotlin.random.Random

/**
 * @author Alice Li
 */
class FibQuestion: Question {

    override fun generateQuestion(): SpecificQuestion {
        return FibSpecificQuestion(Random.nextInt(0, 10), 1, 0)
    }

    override fun getText(): String {
        return """
                private fun mysteriousCode(foo: Int, bar: Int, baz: Int): Int {
                    return when (foo) {
                        0 -> baz
                        1 -> bar
                        else -> {
                            fib(foo - 1, bar + baz, bar)
                        }
                    }
                }
        """.trimIndent()
    }

}

class FibSpecificQuestion(private val n: Int, private val last: Int, private val penultimate: Int) : SpecificQuestion {

    override fun getInput(): String {
        return "$n, $last, $penultimate"
    }

    override fun validate(submittedOutput: String): Boolean {
        return try {
            val intOut = submittedOutput.toInt()
            fib(n, last, penultimate) == intOut
        } catch (th: Throwable) {
            false;
        }
    }

    private fun fib(n: Int, last: Int, penultimate: Int): Int {
        return when (n) {
            0 -> penultimate
            1 -> last
            else -> {
                fib(n - 1, last + penultimate, last)
            }
        }
    }
}

