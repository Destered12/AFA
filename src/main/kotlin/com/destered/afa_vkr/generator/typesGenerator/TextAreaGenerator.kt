package com.destered.afa_vkr.generator.typesGenerator

object TextAreaGenerator {

    var counter = 0

    fun generateTextAreaTest(selector: Pair<String, String>): String {
        counter++
        var code = ""
        var selectorCode = InputGenerator.getSelector(selector)
        if(selectorCode.isNotBlank()) {
            var generatedTest: String = generateTest(selectorCode, "testValue1")
            code += generatedTest
        }
        return code
    }
    fun generateTest(selector: String, expectedText: String): String {
        return """
            @Test
            fun testTextAreaPresence${InputGenerator.counter}() {
                $selector
                assertNotNull(element)
                assertEquals("$expectedText", element.text)
            }
        """.trimIndent()
    }
}