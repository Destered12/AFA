package com.destered.afa_vkr.dialog

import HtmlDialog
import com.destered.afa_vkr.core.DialogController
import com.destered.afa_vkr.model.ItemModel
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class ElementInfoDialog(private val model: ItemModel) : DialogWrapper(true) {
    init {
        title = "Элемент"
        DialogController.addDialogToControl(DialogController.DIALOG_ELEMENT_INFO,this)
        init()
    }

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel(BorderLayout())

        val nameLabel = JLabel("Название: ${model.name}")
        val websiteLabel = JLabel("Сайт: ${model.site}")
        val testButton = JButton("Загрузить HTML код")

        testButton.addActionListener {
            val dialog = HtmlDialog(model)
            dialog.show()
            doOKAction()
        }

        panel.add(nameLabel, BorderLayout.NORTH)
        panel.add(websiteLabel, BorderLayout.CENTER)
        panel.add(testButton, BorderLayout.SOUTH)

        return panel
    }
}