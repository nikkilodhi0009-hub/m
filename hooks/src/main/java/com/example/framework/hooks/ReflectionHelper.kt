package com.example.framework.hooks

import java.lang.reflect.Constructor
import java.lang.reflect.Method

/**
 * Utility helpers for resolving classes, methods, and constructors reflectively.
 */
object ReflectionHelper {
    /**
     * Attempts to resolve a class by name.
     */
    fun findClass(className: String): Class<*>? = try {
        Class.forName(className)
    } catch (_: Throwable) {
        null
    }

    /**
     * Resolves a method by name, optionally matching the supplied parameter types.
     */
    fun findMethod(targetClass: Class<*>, methodName: String, parameterTypes: List<Class<*>> = emptyList()): Method? {
        val candidates = (targetClass.methods + targetClass.declaredMethods)
            .asSequence()
            .filter { it.name == methodName }
            .filter { parameterTypes.isEmpty() || it.parameterTypes.size == parameterTypes.size }
            .filter { parameterTypes.isEmpty() || it.parameterTypes.zip(parameterTypes).all { (actual, expected) -> actual == expected } }
            .toList()
        return candidates.firstOrNull { it.parameterTypes.size == parameterTypes.size }
            ?: candidates.firstOrNull()
    }

    /**
     * Resolves a constructor, optionally matching the supplied parameter types.
     */
    fun findConstructor(targetClass: Class<*>, parameterTypes: List<Class<*>> = emptyList()): Constructor<*>? {
        return targetClass.declaredConstructors.firstOrNull {
            parameterTypes.isEmpty() || (it.parameterTypes.size == parameterTypes.size && it.parameterTypes.zip(parameterTypes).all { (actual, expected) -> actual == expected })
        }
            ?: targetClass.declaredConstructors.firstOrNull()
    }
}
