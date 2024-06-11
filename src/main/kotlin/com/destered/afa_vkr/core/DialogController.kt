package com.destered.afa_vkr.core

import com.intellij.openapi.ui.DialogWrapper

object DialogController {

    const val DIALOG_ENTER = "DIALOG_ENTER"
    const val DIALOG_CREATE_MODEL = "DIALOG_CREATE_MODEL"
    const val DIALOG_ELEMENT_INFO = "DIALOG_ELEMENT_INFO"
    const val DIALOG_HTML_DIALOG = "DIALOG_HTML_DIALOG"
    const val DIALOG_ELEMENT_PREVIEW = "DIALOG_ELEMENT_PREVIEW"

    var dialogList = arrayListOf<Pair<String, DialogWrapper>>()

    fun addDialogToControl(tag: String, dialog: DialogWrapper) {
        dialogList.add(Pair(tag, dialog))
    }

    fun closeDialog(tag: String) {
        dialogList.forEach {
            if (it.first == tag) {
                it.second.close(DialogWrapper.CLOSE_EXIT_CODE)
            }
        }
    }

    fun closeAllDialog() {
        dialogList.forEach {
            if(it.second.isShowing) it.second.close(DialogWrapper.CLOSE_EXIT_CODE)
        }
    }

}