package com.destered.afa_vkr.dialog

import com.destered.afa_vkr.core.DialogController
import com.destered.afa_vkr.model.HtmlElement
import com.destered.afa_vkr.task.GenerateTestsTask
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.*

class HtmlElementPreview(val elements: List<HtmlElement>) : DialogWrapper(true) {
    private val content = JTextArea()

    init {
        title = "Предварительный просмотр элементов HTML"
        DialogController.addDialogToControl(DialogController.DIALOG_ELEMENT_PREVIEW,this)
        setResizable(true)

        // Заполняем текстовую область данными элементов
        elements.forEach { element ->
            content.append("${element.tag}: ${element.text}\n")
        }
        content.isEditable = false
        init() // Инициализируем диалоговое окно
    }

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel(BorderLayout())
        val scrollPane = JScrollPane(content)
        panel.add(scrollPane, BorderLayout.CENTER)
        return panel
    }

    override fun createSouthPanel(): JComponent? {
        val panel = JPanel(BorderLayout())

        // Создание панели для стандартных кнопок
        val buttonsPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        val okButton = JButton("Continue").apply {
            addActionListener { doOKAction() }
        }
        val cancelButton = JButton("Cancel").apply {
            addActionListener { doCancelAction() }
        }

        // Добавление кнопок в панель кнопок
        buttonsPanel.add(okButton)
        buttonsPanel.add(cancelButton)

        // Добавление панели кнопок в основную панель
        panel.add(buttonsPanel, BorderLayout.CENTER)

        return panel
    }

    // Опционально: Можно переопределить метод doOKAction, если нужна специфическая логика при нажатии OK
    override fun doOKAction() {
        // Получите объект Project каким-либо образом, например, из контекста или передайте в конструктор
        val project = ProjectManager.getInstance().defaultProject

        // Запускаем задачу
        ProgressManager.getInstance().run(GenerateTestsTask(project, elements))
        DialogController.closeAllDialog()

        super.doOKAction() // Закрываем диалог, если это необходимо
    }



}