package com.example.framework.hooks

import java.lang.reflect.Constructor
import java.lang.reflect.Method

object ReflectionHelper {
    fun findClass(className: String): Class<*>? = try {
        Class.forName(className)
    } catch (_: Throwable) {
        null
    }

    fun findMethod(targetClass: Class<*>, methodName: String, parameterTypes: List<Class<*>> = emptyList()): Method? {
        val candidates = (targetClass.methods + targetClass.declaredMethods)
            .filter { it.name == methodName }
            .filter { parameterTypes.isEmpty() || it.parameterTypes.size == parameterTypes.size }
            .filter { parameterTypes.isEmpty() || it.parameterTypes.zip(parameterTypes).all { (actual, expected) -> actual == expected } }
        return candidates.firstOrNull { it.parameterTypes.size == parameterTypes.size }
            ?: candidates.firstOrNull()
    }

    fun findConstructor(targetClass: Class<*>, parameterTypes: List<Class<*>> = emptyList()): Constructor<*>? {
        return targetClass.declaredConstructors.firstOrNull {
            parameterTypes.isEmpty() || (it.parameterTypes.size == parameterTypes.size && it.parameterTypes.zip(parameterTypes).all { (actual, expected) -> actual == expected })
        }
            ?: targetClass.declaredConstructors.firstOrNull()
    }
}
