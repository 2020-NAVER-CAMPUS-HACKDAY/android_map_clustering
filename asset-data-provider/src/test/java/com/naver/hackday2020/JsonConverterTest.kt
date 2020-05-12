package com.naver.hackday2020

import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class JsonConverterTest {
    private val jsonSample = """
            {
                "name" : "aaaa",
                "email" : "aaa@bbb.com"
            }
        """.trimIndent()

    @Serializable
    data class Person(val name: String, val email: String, val age: Int = 1)

    @Test
    fun testJsonToObject() {
        val person = JsonConverter.jsonToObject(Person.serializer(), jsonSample)

        assertEquals(person!!.name, "aaaa")
        assertEquals(person.email, "aaa@bbb.com")
        assertEquals(person.age, 1)
    }
}