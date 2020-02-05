package com.genedata.questions

import com.genedata.messages.QuestionEntry
import com.genedata.messages.QuestionsResponse
import com.genedata.models.DB
import com.genedata.models.User
import com.genedata.questions.algorithms.LongestValidParentheses
import com.genedata.questions.algorithms.MedianOfTwoSortedArrays

private typealias QuestionMessage = com.genedata.messages.Question

/**
 * @author Daniel Nesbitt
 */
@Suppress("unused") // Used via enum values
enum class Questions(private val question: Question) : Question {

    MEDIAN_OF_TWO_SORTED_ARRAYS(MedianOfTwoSortedArrays()),
    LONGEST_VALID_PARENS(LongestValidParentheses()),
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

    fun toQuestion(): QuestionMessage = QuestionMessage(ordinal.toLong(), title(), text())

    companion object {
        fun list(user: User): QuestionsResponse {
            val answers = DB.queryAnswers(user.id)
            return QuestionsResponse(values()
                .mapIndexed { index, value ->
                    QuestionEntry(
                        index.toLong(),
                        value.title(),
                        answers.getOrDefault(index.toLong(), false)
                    )
                }
            )
        }

        fun get(id: Long): Questions = values()[id.toInt()]

        fun validate(id: Long, answer: String): Boolean {
            return get(id).validateAnswer(answer)
        }
    }
}
