package com.genedata.messages.generator

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaType

/**
 * @author Daniel Nesbitt
 */
class TSGenerator(klasses: Iterable<KClass<*>>) {

    private val typeConverter = TypeConverter()
    private val visitedClasses = mutableSetOf<KClass<*>>()
    private val definitions = mutableListOf<String>()

    private val ignoredSuperclasses = setOf(
        Any::class,
        java.io.Serializable::class,
        Comparable::class
    )

    init {
        klasses.forEach(this::visitClass)
    }

    val definitionsText: String
        get() = definitions.joinToString(separator = "\n\n")

    private fun visitClass(klass: KClass<*>) {
        if (klass !in visitedClasses) {
            visitedClasses.add(klass)
            if (klass.java.isEnum) {
                definitions.add(generateEnum(klass))
            } else {
                definitions.add(generateInterface(klass))
            }
        }
    }

    private fun convert(kType: KType): TsType {
        val convertedType = typeConverter.convert(kType, this::visitClass)
        val classifier = kType.classifier
        if (classifier is KClass<*> && convertedType.classType) {
            visitClass(classifier)
        }
        return convertedType
    }

    private fun generateEnum(klass: KClass<*>): String {
        val enumContent = klass.java.enumConstants
            .joinToString(separator = "\n", prefix = "{\n", postfix = "\n}") {
                "    $it = \"${it}\","
            }
        return "enum ${klass.simpleName} $enumContent"
    }

    private fun generateInterface(klass: KClass<*>): String {
        val superTypes = klass.supertypes.filterNot { it.classifier in ignoredSuperclasses }
        val extends = if (superTypes.isNotEmpty()) " extends " else "" +
            superTypes.joinToString(separator = " & ") { convert(it).typeName }

        val properties = klass.declaredMemberProperties
            .filter { !isFunctionType(it.returnType.javaType) }
            .filter { it.visibility == KVisibility.PUBLIC }
            .map { property ->
                val propertyType = convert(property.returnType)
                val propertyName = property.name + if (propertyType.optional) "?" else ""
                Pair(propertyName, propertyType.typeName)
            }.toMutableList()

        val isReduxAction = klass.findAnnotation<ReduxAction>() != null

        val lines = mutableListOf<String>()

        if (isReduxAction) {
            lines.add("export const ${klass.simpleName}Type = '${klass.simpleName}';")
            properties.add(Pair("type", "typeof ${klass.simpleName}Type"))
        }
        lines.add("interface ${klass.simpleName}$extends {")
        lines.addAll(properties.map { pair -> "    ${pair.first}: ${pair.second};" })
        lines.add("}")

        return lines.joinToString(separator = "\n")
    }

    private fun isFunctionType(javaType: Type): Boolean {
        return javaType is KCallable<*>
            || javaType.typeName.startsWith("kotlin.jvm.functions.")
            || (javaType is ParameterizedType && isFunctionType(javaType.rawType))
    }

}
