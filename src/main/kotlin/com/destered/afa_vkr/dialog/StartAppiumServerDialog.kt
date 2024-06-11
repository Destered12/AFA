package com.destered.afa_vkr.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.event.ActionEvent
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class StartAppiumServerDialog(private val project: Project?) : DialogWrapper(true) {
    private val contentPane: JPanel = JPanel()

    init {
        init()
        title = "Start Appium Server"
        contentPane.add(JLabel("Do you want to start the Appium server?"))
    }

    override fun createCenterPanel(): JComponent {
        return contentPane
    }

    override fun createActions(): Array<Action> {
        val startServerAction = object : DialogWrapperAction("Start Server") {
            override fun doAction(e: ActionEvent?) {
                close(OK_EXIT_CODE)
            }
        }

        val cancelAction = object : DialogWrapperAction("Cancel") {
            override fun doAction(e: ActionEvent?) {
                close(CANCEL_EXIT_CODE)
            }
        }

        return arrayOf(startServerAction, cancelAction)
    }
}