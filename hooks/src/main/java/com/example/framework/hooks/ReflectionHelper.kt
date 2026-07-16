package com.example.framework.hooks

import java.lang.reflect.Method

object ReflectionHelper {
    fun findClass(className: String): Class<*>? = try {
        Class.forName(className)
    } catch (_: Throwable) {
        null
    }

    fun findMethod(targetClass: Class<*>, methodName: String): Method? {
        val candidates = targetClass.methods.filter { it.name == methodName }
        return candidates.firstOrNull() ?: targetClass.declaredMethods.firstOrNull { it.name == methodName }
    }

    fun findConstructor(targetClass: Class<*>): java.lang.reflect.Constructor<*>? {
        return targetClass.declaredConstructors.firstOrNull()
    }
}
