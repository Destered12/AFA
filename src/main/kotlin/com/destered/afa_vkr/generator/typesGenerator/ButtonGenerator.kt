package com.destered.afa_vkr.generator.typesGenerator

object ButtonGenerator {

    var counter = 0

    fun generateButtonTest(selector: Pair<String, String>): String {
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
            fun testButtonPresence() {
                $selector
                assertNotNull(element)
            }
        """.trimIndent()
    }
}