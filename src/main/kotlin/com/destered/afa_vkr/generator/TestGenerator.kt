package com.destered.afa_vkr.generator

import com.destered.afa_vkr.UtilsFile
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.File


object TestGenerator {

    fun saveTextToFile(relativePath: String, text: String) {
        val project = UtilsFile.project ?: return
        val projectRoot = getProjectPath(project)
        val fullPath = "$projectRoot/$relativePath"
        val file = File(fullPath)

        // Проверяем, существует ли файл, и создаем его, если нет
        if (!file.exists()) {
            println("Файл не найден, создаем новый файл: $fullPath")
            file.parentFile?.mkdirs() // Создаем родительские директории, если они не существуют
            file.createNewFile() // Создаем файл
        }

        // Теперь безопасно записываем содержимое в файл
        file.bufferedWriter().use { out ->
            out.write(text)
        }

        ApplicationManager.getApplication().runWriteAction {
            val virtualFile: VirtualFile? = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
            virtualFile?.refresh(false, false)
        }
    }

    fun getProjectPath(project: Project): String {
        return project.basePath ?: ""
    }



}