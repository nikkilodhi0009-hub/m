package com.example.framework.hooks

import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ReflectionHelperTest {
    @Test
    fun resolvesOverloadedMethodBySignature() {
        val targetClass = TestTarget::class.java
        val method = ReflectionHelper.findMethod(targetClass, "doWork", listOf(String::class.java, Int::class.java))

        assertNotNull(method)
        assertTrue(method.name == "doWork")
    }

    @Test
    fun resolvesConstructor() {
        val ctor = ReflectionHelper.findConstructor(TestTarget::class.java, listOf(String::class.java))
        assertNotNull(ctor)
    }

    private class TestTarget {
        fun doWork(value: String) {}
        fun doWork(value: String, count: Int) {}
        constructor() : this("")
        constructor(value: String) {}
    }
}
