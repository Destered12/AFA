package com.destered.afa_vkr.dialog

import com.example.plugin.AppiumInstallationDialog
import com.intellij.openapi.ui.DialogWrapper
import java.awt.event.ActionEvent
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class AppiumCheckDialog : DialogWrapper(true) {
    private var contentPane: JPanel = JPanel()

    init {
        init()
        title = "Appium не найден"
        contentPane.add(JLabel("Не удалось обнаружить appium в системе"))
    }

    override fun createCenterPanel(): JComponent {
        return contentPane
    }

    override fun createActions(): Array<Action> {
        val continueAction = object : DialogWrapperAction("Продолжить без Appium") {
            override fun doAction(e: ActionEvent?) {
                close(OK_EXIT_CODE)
            }
        }

        val installGuideAction = object : DialogWrapperAction("Установка") {
            override fun doAction(e: ActionEvent?) {
                val installationDialog = AppiumInstallationDialog()
                installationDialog.show()
                close(OK_EXIT_CODE)
            }
        }

        return arrayOf(continueAction, installGuideAction)
    }
}