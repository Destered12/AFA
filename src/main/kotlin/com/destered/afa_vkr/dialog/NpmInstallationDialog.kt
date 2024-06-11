package com.destered.afa_vkr.dialog

import com.intellij.openapi.ui.DialogWrapper
import java.awt.event.ActionEvent
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class NpmInstallationDialog : DialogWrapper(true) {
    private var contentPane: JPanel = JPanel()

    init {
        init()
        title = "NPM Not Found"
        contentPane.add(JLabel("NPM is not installed on your system."))
    }

    override fun createCenterPanel(): JComponent {
        return contentPane
    }

    override fun createActions(): Array<Action> {
        val manualInstallAction = object : DialogWrapperAction("Manual Installation") {
            override fun doAction(e: ActionEvent?) {
                // Открыть URL инструкции по установке npm
                java.awt.Desktop.getDesktop().browse(java.net.URI("https://docs.npmjs.com/downloading-and-installing-node-js-and-npm"))
                close(OK_EXIT_CODE)
            }
        }

        val continueWithoutAppiumAction = object : DialogWrapperAction("Continue Without Appium") {
            override fun doAction(e: ActionEvent?) {
                close(OK_EXIT_CODE)
            }
        }

        return arrayOf(manualInstallAction, continueWithoutAppiumAction)
    }
}