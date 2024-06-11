package com.destered.afa_vkr.generator.typesGenerator

object FormGenerator {

    var counter = 0

    fun generateFormTest(selector: Pair<String, String>): String {
        counter++
        var code = ""
        var selectorCode = InputGenerator.getSelector(selector)
        if(selectorCode.isNotBlank()) {
            var generatedTest: String = generateTest(selectorCode)
            code += generatedTest
        }
        return code
    }
    fun generateTest(selector: String): String {
        return """
            @Test
            fun testFormPresence$counter() {
                $selector
                assertNotNull(element)
            }
        """.trimIndent()
    }
}