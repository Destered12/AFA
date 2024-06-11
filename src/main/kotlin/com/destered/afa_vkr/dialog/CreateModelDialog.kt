package com.destered.afa_vkr.dialog

import com.destered.afa_vkr.core.DialogController
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class CreateModelDialog : DialogWrapper(true) {
    var enteredName: String = ""
    var enteredWebsite: String = ""

    init {
        title = "Введите данные"
        DialogController.addDialogToControl(DialogController.DIALOG_CREATE_MODEL,this)
        init()
    }

    private lateinit var nameField: JTextField
    private lateinit var websiteField: JTextField

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel(BorderLayout())

        val nameLabel = JLabel("Название:")
        nameField = JTextField()
        val namePanel = JPanel(BorderLayout())
        namePanel.add(nameLabel, BorderLayout.WEST)
        namePanel.add(nameField, BorderLayout.CENTER)

        val websiteLabel = JLabel("Сайт:")
        websiteField = JTextField()
        val websitePanel = JPanel(BorderLayout())
        websitePanel.add(websiteLabel, BorderLayout.WEST)
        websitePanel.add(websiteField, BorderLayout.CENTER)

        panel.add(namePanel, BorderLayout.NORTH)
        panel.add(websitePanel, BorderLayout.SOUTH)

        return panel
    }

    override fun doOKAction() {
        enteredName = nameField.text
        enteredWebsite = websiteField.text
        super.doOKAction()
    }
}