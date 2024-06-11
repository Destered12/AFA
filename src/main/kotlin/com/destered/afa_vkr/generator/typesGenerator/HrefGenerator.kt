package com.destered.afa_vkr.generator.typesGenerator

object HrefGenerator {

    var counter = 0

    fun generateHrefTest(selector: Pair<String, String>): String {
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
            fun testLinkPresence$counter() {
                $selector
                assertNotNull(element)
            }
        """.trimIndent()
    }
}