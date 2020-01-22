package com.genedata.questions

/**
 * @author Alice Li
 */
interface Question {

    fun generateQuestion(): SpecificQuestion
    fun getText(): String

}
