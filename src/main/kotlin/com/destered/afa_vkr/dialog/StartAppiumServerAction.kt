package com.destered.afa_vkr.dialog

import com.destered.afa_vkr.AppiumUtils
import com.intellij.execution.impl.ConsoleViewImpl
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.Key
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager

class StartAppiumServerAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val dialog = StartAppiumServerDialog(project)
        if (dialog.showAndGet()) {
            // Логика запуска сервера Appium
            startAppiumServer(project)
        }
    }

    private fun startAppiumServer(project: Project?) {
        if (project != null) {
            val console = createConsole(project)
            val toolWindow = getToolWindow(project, console)
            toolWindow.show {
                runAppiumServer(console)
            }
        }
    }

    private fun createConsole(project: Project): ConsoleView {
        return ConsoleViewImpl(project, false)
    }

    private fun getToolWindow(project: Project, console: ConsoleView): ToolWindow {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        var toolWindow = toolWindowManager.getToolWindow("Appium Server")
        if (toolWindow == null) {
            toolWindow = toolWindowManager.registerToolWindow("Appium Server", true, ToolWindowAnchor.BOTTOM)
        }
        val content = toolWindow.contentManager.factory.createContent(console.component, "", false)
        toolWindow.contentManager.addContent(content)
        return toolWindow
    }

    private fun runAppiumServer(console: ConsoleView) {
        try {
            val path = AppiumUtils.getAppium()
            val processBuilder = ProcessBuilder("$path")
            val process = processBuilder.start()
            val processHandler = OSProcessHandler(process, "Appium Server")
            processHandler.addProcessListener(object : ProcessListener {
                override fun startNotified(event: ProcessEvent) {
                    console.print("Starting Appium Server...\n", ConsoleViewContentType.SYSTEM_OUTPUT)
                }

                override fun processTerminated(event: ProcessEvent) {
                    console.print("Appium Server stopped.\n", ConsoleViewContentType.SYSTEM_OUTPUT)
                }

                override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {}

                override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                    console.print(event.text, ConsoleViewContentType.NORMAL_OUTPUT)
                }
            })
            processHandler.startNotify()
        } catch (e: Exception) {
            Messages.showMessageDialog(null, "Failed to start Appium server: ${e.message}", "Error", Messages.getErrorIcon())
        }
    }
}