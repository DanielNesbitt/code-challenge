package com.genedata.messages.generator

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaType

/**
 * @author Daniel Nesbitt
 */
class TSGenerator(klasses: Iterable<KClass<*>>) {

    private val typeConverter = TypeConverter()
    private val visitedClasses = mutableSetOf<KClass<*>>()
    private val definitions = mutableListOf<String>()
    private val ActionType = ReduxAction::class.createType()
    private val SocketActionType = SocketAction::class.createType()

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
        val superTypes = klass.supertypes.filterNot { it.classifier in ignoredSuperclasses }.toMutableList()
        val isSocketAction = superTypes.remove(SocketActionType)
        val isReduxAction = isSocketAction || superTypes.remove(ActionType)
        var extends = if (superTypes.isNotEmpty()) " extends " else "" +
            superTypes.joinToString(separator = " & ") { convert(it).typeName }


        val properties = klass.declaredMemberProperties
            .filter { !isFunctionType(it.returnType.javaType) }
            .filter { it.visibility == KVisibility.PUBLIC }
            .map { property ->
                val propertyType = convert(property.returnType)
                val propertyName = property.name + if (propertyType.optional) "?" else ""
                Pair(propertyName, propertyType.typeName)
            }.toMutableList()

        val lines = mutableListOf<String>()

        val name = klass.simpleName
        if (isReduxAction) {
            extends += " extends Action<typeof ${name}Type>"
            lines.add("export const ${name}Type = '$name';")
            lines.add("type ${name}ReduxAction = Action<typeof ${name}Type>;")
            properties.add(Pair("type", "typeof ${name}Type"))
        }
        lines.add("export interface $name$extends {")
        lines.addAll(properties.map { pair -> "    ${pair.first}: ${pair.second};" })
        lines.add("}")
        if (isReduxAction) {
            lines.add("export type ${name}Action = ${name} & ${name}ReduxAction;")
        }
        if (isSocketAction) {
            lines.add("export function create${name}Action(arg: ${name}): ${name}Action {")
            lines.add("    return {")
            lines.add("        ...arg,")
            lines.add("        type: ${name}Type,")
            lines.add("    }")
            lines.add("}")
        }

        return lines.joinToString(separator = "\n")
    }

    private fun isFunctionType(javaType: Type): Boolean {
        return javaType is KCallable<*>
            || javaType.typeName.startsWith("kotlin.jvm.functions.")
            || (javaType is ParameterizedType && isFunctionType(javaType.rawType))
    }

}
