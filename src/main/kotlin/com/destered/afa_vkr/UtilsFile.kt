package com.destered.afa_vkr

import com.destered.afa_vkr.generator.typesGenerator.*
import com.destered.afa_vkr.model.ItemModel
import com.google.gson.Gson
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import java.io.File

object UtilsFile {

    var project: Project? = null

    fun loadStrings(): List<ItemModel> {
        val file = File("savedStrings.json")
        val listModel = arrayListOf<ItemModel>()
        if (file.exists()) {
            val json = FileUtil.loadFile(file, Charsets.UTF_8)
            val gson = Gson()
            val stringModels = gson.fromJson(json, Array<ItemModel>::class.java)
            stringModels.forEach { listModel.add(it) }
        }
        return listModel
    }

    fun clearGeneratorCounter() {
        InputGenerator.counter = 0
        SelectGenerator.counter = 0
        ButtonGenerator.counter = 0
        FormGenerator.counter = 0
        HrefGenerator.counter = 0
        TextAreaGenerator.counter = 0
    }

    fun saveStrings(list: List<ItemModel>) {
        val file = File("savedStrings.json")
        val gson = Gson()
        val json = gson.toJson(list.toTypedArray())
        FileUtil.writeToFile(file, json, Charsets.UTF_8)
    }

    fun deleteStrings(element: ItemModel) {
       val list = arrayListOf<ItemModel>()
        list.addAll(loadStrings())
        list.remove(element)
        saveStrings(list)
    }
}

