package com.example.framework.hooks

object AndroidVersionCompat {
    fun isApi29OrHigher(): Boolean = true

    fun resolveClassName(className: String): String = className

    fun resolveMethodName(methodName: String): String = methodName
}
