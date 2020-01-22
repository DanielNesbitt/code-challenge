package com.genedata.questions

/**
 * @author Alice Li
 */
interface SpecificQuestion {

    fun getInput(): String;
    fun validate(submittedOutput: String): Boolean;

}
