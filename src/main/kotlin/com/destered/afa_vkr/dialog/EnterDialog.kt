package com.destered.afa_vkr.dialog

import com.destered.afa_vkr.UtilsFile
import com.destered.afa_vkr.core.DialogController
import com.destered.afa_vkr.model.ItemModel
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.Component
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class EnterDialog : DialogWrapper(true) {
    private lateinit var listModel: DefaultListModel<ItemModel>
    private lateinit var list: JList<ItemModel>

    init {
        title = "Сценарии"
        DialogController.addDialogToControl(DialogController.DIALOG_ENTER,this)
        init()
    }

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel(BorderLayout())
        listModel = DefaultListModel()
        list = JList(listModel)
        list.cellRenderer = MyListCellRenderer() // Установка кастомного рендерера для JList
        list.addMouseListener(MyMouseListener()) // Добавление слушателя событий к списку
        val scrollPane = JScrollPane(list)
        panel.add(scrollPane, BorderLayout.CENTER)
        val addButton = JButton("Добавить элемент")
        val deleteButton = JButton("Удалить элемент (Del)")
        val buttonPanel = JPanel()
        buttonPanel.add(addButton)
        buttonPanel.add(deleteButton)
        panel.add(buttonPanel, BorderLayout.SOUTH)
        addButton.addActionListener { addElement() }
        loadStrings()


        list.addListSelectionListener { _ ->
            deleteButton.isEnabled = list.selectedIndices.isNotEmpty() // Включаем кнопку удаления, если выбран хотя бы один элемент
        }
        initDeleteButton(deleteButton)
        return panel
    }

    private fun initDeleteButton(deleteButton: JButton) {
        deleteButton.isEnabled = false // Начально отключаем кнопку удаления
        deleteButton.addActionListener {
            val selectedIndex = list.selectedIndex
            if (selectedIndex != -1) {
                val element = listModel[selectedIndex]
                UtilsFile.deleteStrings(element)
                listModel.removeElementAt(selectedIndex)
            }
        }
        list.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {}

            override fun keyPressed(e: KeyEvent?) {}

            override fun keyReleased(e: KeyEvent?) {
                if (e?.keyCode == KeyEvent.VK_DELETE) {
                    val selectedIndex = list.selectedIndex
                    if (selectedIndex != -1) {
                        val element = listModel[selectedIndex]
                        UtilsFile.deleteStrings(element)
                        listModel.removeElementAt(selectedIndex)
                    }
                }
            }
        })
    }

    private fun addElement() {
        val dialog = CreateModelDialog()
        if (dialog.showAndGet()) {
            val element = ItemModel(dialog.enteredName, dialog.enteredWebsite)
            listModel.addElement(element)
            saveStrings()
        }
    }

    private fun loadStrings() {
        UtilsFile.loadStrings().forEach { listModel.addElement(it) }
    }

    private fun saveStrings() {
        UtilsFile.saveStrings(listModel.elements().toList())
    }

    private inner class MyListCellRenderer : DefaultListCellRenderer() {
        override fun getListCellRendererComponent(
            list: JList<*>?,
            value: Any?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
        ): Component {
            val component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
            if (value is ItemModel) {
                text = value.name
            }
            return component
        }
    }

    // Внутренний класс для обработки двойного клика по элементу в списке
    private inner class MyMouseListener : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            if (e.clickCount == 2) { // Проверяем, что это двойной клик
                val selectedElement = list.selectedValue // Получаем выбранный элемент
                if (selectedElement != null) {
                    val dialog = ElementInfoDialog(selectedElement)
                    dialog.show()
                    doOKAction()
                }
            }
        }
    }
}