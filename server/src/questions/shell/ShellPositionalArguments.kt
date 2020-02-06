package com.genedata.questions.shell

import com.genedata.questions.Question

/**
 * @author Daniel Nesbitt
 */
class ShellPositionalArguments: Question {

    override fun title(): String ="Shell: Arguments"

    override fun text(): String = """
        Given the following script `execute.sh`.
        ```bash
        #!/bin/bash

        while [ "${'$'}1" != "" ]; do
            case ${'$'}1 in
                d )	shift
                        shift
                        echo ${'$'}1
                        ;;
                f | g )	;;
                * )		echo ${'$'}1
                        ;;
            esac
            shift
        done
        ```
        What value is printed on the 5th line output when executed as follows?
         ```bash
         ./execute.sh a b c d e f g h i j k 
         ```
    """.trimIndent()

    override fun validateAnswer(answer: String): Boolean {
        return answer.trim().equals("h", true)
    }

}
