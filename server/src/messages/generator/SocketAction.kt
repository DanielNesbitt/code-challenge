package com.genedata.messages.generator

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.genedata.messages.Answer

/**
 * @author Daniel Nesbitt
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(Answer::class, name = "Answer")
)
@JsonIgnoreProperties(ignoreUnknown = true)
interface SocketAction : ReduxAction
