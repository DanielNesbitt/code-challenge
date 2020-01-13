package com.genedata.messages.generator

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf

/**
 * @author Daniel Nesbitt
 */
sealed class TsType(val typeName: String, val optional: Boolean, val classType: Boolean = false)

class BasicType internal constructor(typeName: String, nullable: Boolean = false) : TsType(typeName, nullable)
class ClassType internal constructor(typeName: String, nullable: Boolean) : TsType(typeName, nullable, true)
private class ArrayType(type: TsType, nullable: Boolean = false) : TsType(type.typeName + "[]", nullable)
private class MapType(keyType: TsType, valueType: TsType, nullable: Boolean = false) :
    TsType("{ [key: ${keyType.typeName}]: ${valueType.typeName} }", nullable)

private val NullableAny = Any::class.createType(nullable = true)

class TypeConverter {

    private val mappings = mutableMapOf<KType, TsType>()

    fun convert(kType: KType): TsType {
        return mappings.getOrPut(kType, { generateType(kType) })
    }

    private fun generateType(kType: KType): TsType {
        val nullable = kType.isMarkedNullable
        return when (val classifier = kType.classifier) {
            String::class, Char::class -> BasicType("string", nullable)
            Boolean::class -> BasicType("boolean", nullable)
            Int::class, Long::class, Short::class, Byte::class,
            Float::class, Double::class -> BasicType("number", nullable)
            Any::class -> BasicType("any", nullable)
            is KClass<*> -> {
                return if (classifier.isSubclassOf(Iterable::class) || classifier.javaObjectType.isArray) {
                    val arrayType = kType.arguments.single().type ?: NullableAny
                    ArrayType(generateType(arrayType), nullable)
                } else if (classifier.isSubclassOf(Map::class)) {
                    val keyType = kType.arguments[0].type
                    if (keyType?.classifier!! != String::class) {
                        throw RuntimeException("Only maps with String keys are supported.")
                    }
                    val valueType = kType.arguments[1].type ?: NullableAny
                    MapType(convert(keyType), convert(valueType), nullable)
                } else {
                    ClassType(classifier.simpleName!!, nullable)
                }
            }
            is KTypeParameter -> BasicType(classifier.name, nullable)
            else -> throw RuntimeException("Could not match type.")
        }
    }

}
