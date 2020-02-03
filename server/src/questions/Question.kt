package com.genedata.questions

/**
 * @author Daniel Nesbitt
 */
interface Question {

    fun title(): String
    fun text(): String

    fun validateAnswer(answer: String): Boolean

}
