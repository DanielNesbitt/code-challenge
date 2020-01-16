package com.genedata.messages.generator

import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * @author Daniel Nesbitt
 */
@Suppress("unused")
interface ReduxAction {

    @JsonSerialize
    fun type(): String {
        return this::class.simpleName!!
    }

}
