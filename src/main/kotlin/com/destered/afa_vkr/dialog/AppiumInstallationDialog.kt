package com.example.plugin

import com.destered.afa_vkr.AppiumUtils
import com.destered.afa_vkr.dialog.NpmInstallationDialog
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import java.awt.event.ActionEvent
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class AppiumInstallationDialog : DialogWrapper(true) {
    private var contentPane: JPanel = JPanel()

    init {
        init()
        title = "Установка Appium"
        contentPane.add(JLabel("Выберите метод установки Appium:"))
    }

    override fun createCenterPanel(): JComponent {
        return contentPane
    }

    override fun createActions(): Array<Action> {
        val manualInstallAction = object : DialogWrapperAction("Ручная установка") {
            override fun doAction(e: ActionEvent?) {
                // Открыть URL инструкции по установке Appium
                java.awt.Desktop.getDesktop().browse(java.net.URI("https://appium.io/docs/en/latest/quickstart/"))
                close(OK_EXIT_CODE)
            }
        }

        val automaticInstallAction = object : DialogWrapperAction("Автоматическая установка") {
            override fun doAction(e: ActionEvent?) {
                if (AppiumUtils.getNpm() != null) {
                    installAppiumAutomatically()
                } else {
                    val npmDialog = NpmInstallationDialog()
                    npmDialog.show()
                }
                close(OK_EXIT_CODE)
            }
        }

        return arrayOf(manualInstallAction, automaticInstallAction)
    }

    private fun installAppiumAutomatically() {
        ProgressManager.getInstance().run(object : Task.Modal(null, "Installing Appium", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.text = "Installing Appium..."
                indicator.isIndeterminate = true

                val npmPath = AppiumUtils.getNpm()
                if (npmPath != null) {
                    try {
                        val process = Runtime.getRuntime().exec("$npmPath install -g appium")
                        process.waitFor()
                        val exitValue = process.exitValue()
                        if (exitValue == 0) {
                            showInstallationComplete("Appium installed successfully.", "Installation Complete")
                        } else {
                            showInstallationComplete("Failed to install Appium.", "Installation Failed")
                        }
                    } catch (e: Exception) {
                        showInstallationComplete("Error occurred while installing Appium: ${e.message}", "Installation Error")
                    }
                } else {
                    showInstallationComplete("NPM not found. Cannot install Appium automatically.", "Installation Error")
                }
            }
        })
    }

    private fun showInstallationComplete(message: String, title: String) {
        ApplicationManager.getApplication().invokeLater {
            Messages.showMessageDialog(null, message, title, Messages.getInformationIcon())
        }
    }
}