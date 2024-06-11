package com.destered.afa_vkr.task

import com.destered.afa_vkr.UtilsFile
import com.destered.afa_vkr.generator.TestGenerator
import com.destered.afa_vkr.generator.core.CoreGenerator
import com.destered.afa_vkr.generator.typesGenerator.ButtonGenerator.generateButtonTest
import com.destered.afa_vkr.generator.typesGenerator.FormGenerator.generateFormTest
import com.destered.afa_vkr.generator.typesGenerator.HrefGenerator.generateHrefTest
import com.destered.afa_vkr.generator.typesGenerator.InputGenerator.generateInputTest
import com.destered.afa_vkr.generator.typesGenerator.SelectGenerator.generateSelectTest
import com.destered.afa_vkr.generator.typesGenerator.TextAreaGenerator.generateTextAreaTest
import com.destered.afa_vkr.model.HtmlElement
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class GenerateTestsTask(project: Project, private val elements: List<HtmlElement>) :
    Task.Modal(project, "Генерация автотестов", true) {

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = true // Устанавливаем индикатор выполнения без конкретных шагов
        generateAutoTests("TestClass", elements, indicator) // Метод, где выполняется ваша логика генерации
    }

    private fun generateAutoTests(className: String, elements: List<HtmlElement>, indicator: ProgressIndicator) {
        var testCode = CoreGenerator.generateTestHeader(className)
        val relativePath = "app/src/test/java/$className.java"
        var maxSteps = elements.size + 1
        var currentStep = 1
        UtilsFile.clearGeneratorCounter()

        for (element in elements) {
            indicator.fraction = currentStep.toDouble() / maxSteps
            indicator.text = "Анализ ${element.text}"
            val elementSelector = findElementSelector(element)

            testCode += when (element.tag) {
                "input" -> {
                    generateInputTest(elementSelector)
                }

                "button" -> {generateButtonTest(elementSelector)}
                "a" -> {generateHrefTest(elementSelector)}
                "select" -> {generateSelectTest(elementSelector)}
                "textarea" -> {generateTextAreaTest(elementSelector)}
                "form" -> {generateFormTest(elementSelector)}
                else -> {}
            }

            currentStep++
        }

        testCode += CoreGenerator.generateTestFooter()
        indicator.fraction = 1.0
        indicator.text = "Создание файла $className.java"
        TestGenerator.saveTextToFile(relativePath, testCode)
        Thread.sleep(2000)
    }

    fun findElementSelector(element: HtmlElement): Pair<String, String> {

        element.attributes["id"]?.let {
            return Pair("id", it)
        }

        element.attributes["name"]?.let {
            return Pair("name", it)
        }

        return Pair("", "")
    }
}