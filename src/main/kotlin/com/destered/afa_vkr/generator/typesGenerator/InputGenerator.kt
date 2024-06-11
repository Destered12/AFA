package com.destered.afa_vkr.generator.typesGenerator

object InputGenerator {

    var counter = 0

    fun generateInputTest(selector: Pair<String, String>): String {
        counter++
        var code = ""
        var selectorCode = getSelector(selector)
        if(selectorCode.isNotBlank()) {
            var generatedTest: String = generateTestForInput(selectorCode, "testValue1")
            generatedTest += generateTestForCopyPaste(selectorCode, "testValue2")
            code += generatedTest
        }
        return code
    }

    fun getSelector(selector: Pair<String, String>): String {
       return when(selector.first) {
           "id" -> {"WebElement element = driver.findElement(By.id(\"${selector.second}\"));"}
           "name" -> {"WebElement element = driver.findElement(By.name(\"${selector.second}\"));"}
           else -> { "" }
        }
    }

    fun generateTestForInput(selector: String, inputValue: String): String {
        return """
    @Test
    public void testInputById$counter() {
        $selector
        element.sendKeys("$inputValue");
        assert inputField.getText().equals("$inputValue");
    }
        
    """.trimIndent()
    }


    fun generateTestForCopyPaste(selector: String, inputValue: String): String {
        return """
                 @Test
    public void testCopyPaste$counter() {
        String textToCopy = "$inputValue";
        
        $selector
        inputField.sendKeys(textToCopy);
        inputField.sendKeys(Keys.CONTROL + "a"); // Выделить все
        inputField.sendKeys(Keys.CONTROL + "x"); // Копировать

        inputField.sendKeys(Keys.CONTROL + "v");
        Assert.assertEquals("Текст во втором поле ввода не соответствует скопированному тексту", textToCopy, inputField.getText());
                }
                
        """.trimIndent()
    }



}