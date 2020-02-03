package com.genedata.questions.algorithms

import com.genedata.questions.Question

/**
 * @author Daniel Nesbitt
 */
class MedianOfTwoSortedArrays: Question {

    override fun title(): String {
        return "Leetcode Hard"
    }

    override fun text(): String {
        val simpleName = this.javaClass.simpleName
        return this.javaClass.getResource("${simpleName}.md").readText()

    }

    override fun validateAnswer(answer: String): Boolean {
        return false
    }


}
