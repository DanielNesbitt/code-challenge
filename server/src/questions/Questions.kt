package com.genedata.questions

import com.genedata.messages.QuestionEntry
import com.genedata.messages.QuestionsResponse
import com.genedata.models.DB
import com.genedata.models.User
import com.genedata.questions.algorithms.LongestValidParentheses
import com.genedata.questions.algorithms.MedianOfTwoSortedArrays
import com.genedata.questions.java.JavaStreams
import com.genedata.questions.java.WhatDoesItDo
import com.genedata.questions.shell.ShellPositionalArguments
import com.genedata.questions.sql.SQLCommonJoin
import com.genedata.questions.sql.SQLFlowControl
import com.genedata.questions.sql.SQLWindowing
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private typealias QuestionMessage = com.genedata.messages.Question

/**
 * @author Daniel Nesbitt
 */
@Suppress("unused") // Used via enum values
enum class Questions(private val question: Question) : Question {

    // Java
    JAVA_STREAMS(JavaStreams()),
    WHAT_DOES_IT_DO(WhatDoesItDo()),

    // Algorithms
    MEDIAN_OF_TWO_SORTED_ARRAYS(MedianOfTwoSortedArrays()),
    LONGEST_VALID_PARENS(LongestValidParentheses()),

    // SQL
    SQL_COMMON_JOIN(SQLCommonJoin()),
    SQL_WINDOWING(SQLWindowing()),
    SQL_FLOW_CONTROL(SQLFlowControl()),

    // Shell
    SHELL_POS_ARGS(ShellPositionalArguments())
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
        private val logger: Logger = LoggerFactory.getLogger(Questions::class.java)

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
            return try {
                get(id).validateAnswer(answer)
            } catch (th: Throwable) {
                logger.warn("Could not validate answer ($answer) for question (${id}).")
                logger.debug("Answer validation problem.", th)
                false
            }
        }
    }
}
