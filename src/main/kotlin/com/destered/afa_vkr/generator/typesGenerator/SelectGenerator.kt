package com.destered.afa_vkr.generator.typesGenerator

object SelectGenerator {

    var counter = 0

    fun generateSelectTest(selector: Pair<String, String>): String {
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
            fun testSelectPresence$counter() {
                $selector
                assertNotNull(element)
            }
        """.trimIndent()
    }
}