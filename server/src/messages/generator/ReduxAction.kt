package com.genedata.messages.generator

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * @author Daniel Nesbitt
 */
@JacksonAnnotationsInside
@JsonSerialize(using = ReduxActionSerializer::class)
@Target(AnnotationTarget.CLASS)
annotation class ReduxAction
