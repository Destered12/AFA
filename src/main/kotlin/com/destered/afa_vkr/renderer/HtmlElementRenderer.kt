package com.destered.afa_vkr.renderer

import com.destered.afa_vkr.model.HtmlElement
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer

class HtmlElementRenderer : ListCellRenderer<HtmlElement> {
    private val defaultRenderer = DefaultListCellRenderer()

    override fun getListCellRendererComponent(
        list: JList<out HtmlElement>?,
        value: HtmlElement?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val renderer = defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as JLabel
        if (value != null) {
            renderer.text = "${value.tag}: ${value.text}"
        }
        return renderer
    }
}