package com.genedata.questions

import com.genedata.messages.QuestionEntry
import com.genedata.messages.QuestionsResponse
import com.genedata.questions.algorithms.MedianOfTwoSortedArrays

/**
 * @author Daniel Nesbitt
 */
enum class Questions(private val question: Question) : Question {

    MEDIAN_OF_TWO_SORTED_ARRAYS(MedianOfTwoSortedArrays()),
    ;

    override fun title(): String {
        return question.title()
    }

    override fun text(): String {
        return question.text()
    }

    override fun validateAnswer(answer: String): Boolean {
        return question.validateAnswer(answer)
    }

    fun toQuestion(): com.genedata.messages.Question {
        return com.genedata.messages.Question(ordinal.toLong(), title(), text())
    }

    companion object {
        fun list(): QuestionsResponse {
            return QuestionsResponse(values()
                .mapIndexed { index, value -> QuestionEntry(index.toLong(), value.title()) }
            )
        }

        fun get(id: Long): Questions = values()[id.toInt()]

        fun validate(id: Long, answer: String): Boolean {
            return get(id).validateAnswer(answer)
        }
    }

}
