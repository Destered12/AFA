package com.destered.afa_vkr

import java.io.BufferedReader
import java.io.InputStreamReader


object AppiumUtils {
    fun getAppium(): String? {
        return try {
            val process = Runtime.getRuntime().exec("where appium") // Для Windows, используйте "which npm" для Unix-подобных систем
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var path = reader.readLine()
            if(!path.isNullOrBlank() && !path.contains(".cmd")) path+= ".cmd"
            path = path.replace("\\","\\\\")
            process.waitFor()
            path
        } catch (e: Exception) {
            null
        }
    }

    fun getNpm(): String? {
        return try {
            val process = Runtime.getRuntime().exec("where npm") // Для Windows, используйте "which npm" для Unix-подобных систем
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var path = reader.readLine()
            if(!path.isNullOrBlank() && !path.contains(".cmd")) path+= ".cmd"
            path = path.replace("\\","\\\\")
            process.waitFor()
            path
        } catch (e: Exception) {
            null
        }
    }
}