import com.destered.afa_vkr.core.DialogController
import com.destered.afa_vkr.dialog.HtmlElementPreview
import com.destered.afa_vkr.model.HtmlElement
import com.destered.afa_vkr.model.ItemModel
import com.intellij.openapi.ui.DialogWrapper
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URL
import javax.swing.*

class HtmlDialog(private val model: ItemModel) : DialogWrapper(true) {
    private val elementsByTag = mutableMapOf<String, MutableList<HtmlElement>>()
    private val listModel = DefaultListModel<HtmlElement>()
    private val list = JList(listModel)
    private val tagCheckboxesPanel = JPanel()
    private val tagsForTesting = setOf("input", "button", "a", "select", "textarea", "form")
    private val filterCheckbox = JCheckBox("Показать только теги для автотестов")

    init {
        title = "HTML Содержимое"
        DialogController.addDialogToControl(DialogController.DIALOG_HTML_DIALOG,this)
        init()
    }

    override fun createCenterPanel(): JComponent {
        val mainPanel = JPanel(BorderLayout())
        parseHtmlAndFillElements()

        val toggleSelectionButton = JButton("Выбрать всё").apply {
            addActionListener {
                val selectAll = text == "Выбрать всё"
                text = if (selectAll) "Снять выбор со всех" else "Выбрать всё"
                tagCheckboxesPanel.components.filterIsInstance<JCheckBox>().forEach { it.isSelected = selectAll }
                updateVisibleElements()
            }
        }

        filterCheckbox.addItemListener {
            updateTagCheckboxesPanel() // Обновить панель с чекбоксами при изменении состояния чекбокса фильтра
        }

        // Создание текстовой метки
        val label = JLabel("Выберите теги для автотеста").apply {
            // Настройка внешнего вида метки, если требуется
        }

        // Панель для размещения метки, кнопки и чекбокса
        val controlsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(label) // Добавление метки на панель
            add(toggleSelectionButton) // Добавление кнопки на панель
            add(filterCheckbox) // Добавление чекбокса на панель
        }

        list.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    val index = list.locationToIndex(e.point)
                    if (index >= 0) {
                        val htmlElement = list.model.getElementAt(index)
                        HtmlElementDetailsDialog(htmlElement).isVisible = true
                    }
                }
            }
        })

        val scrollPaneCheckboxes = JScrollPane(tagCheckboxesPanel).apply {
            preferredSize = Dimension(200, 200)
        }
        mainPanel.add(controlsPanel, BorderLayout.NORTH)
        mainPanel.add(scrollPaneCheckboxes, BorderLayout.WEST)

        val scrollPaneList = JScrollPane(list).apply {
            preferredSize = Dimension(400, 200)
        }
        mainPanel.add(scrollPaneList, BorderLayout.CENTER)

        return mainPanel
    }

    override fun createSouthPanel(): JComponent? {
        val panel = JPanel(BorderLayout())

        // Создание текстовой метки
        val label = JLabel("Выберите теги для автотеста").apply {
            // Настройка внешнего вида метки
        }

        // Добавление метки в панель
        panel.add(label, BorderLayout.WEST)

        // Создание панели для стандартных кнопок
        val buttonsPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        val okButton = JButton("OK").apply {
            addActionListener { doOKAction() }
        }
        val cancelButton = JButton("Cancel").apply {
            addActionListener { doCancelAction() }
        }

        // Добавление кнопок в панель кнопок
        buttonsPanel.add(okButton)
        buttonsPanel.add(cancelButton)

        // Добавление панели кнопок в основную панель
        panel.add(buttonsPanel, BorderLayout.CENTER)

        return panel
    }

    override fun doOKAction() {
        val selectedElements = mutableListOf<HtmlElement>()

        elementsByTag.forEach { (tag, elements) ->
            val checkBox =
                tagCheckboxesPanel.components.find { it is JCheckBox && it.text == tag && it.isSelected } as? JCheckBox
            if (checkBox?.isSelected == true) {
                selectedElements.addAll(elements)
            }
        }

        if (selectedElements.isNotEmpty()) {
            HtmlElementPreview(selectedElements).show()
        } else {
            JOptionPane.showMessageDialog(
                null,
                "Пожалуйста, выберите хотя бы один тег.",
                "Внимание",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun parseHtmlAndFillElements() {
        try {
            val url = URL(model.site)
            val connection = url.openConnection()
            connection.getInputStream().use { inputStream ->
                val document = Jsoup.parse(inputStream, "UTF-8", url.toString())
                // Рекурсивный обход документа начиная с body
                parseElement(document.body())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun parseElement(element: Element) {
        // Пропускам script и style теги
        if (element.tagName().equals("script", ignoreCase = true) || element.tagName().equals("style", ignoreCase = true)) return

        // Добавляем элемент в map
        val tag = element.tagName()
        val text = element.text().trim()
        val fullText = element.outerHtml().trim()
        val attributes = element.attributes().associate { it.key to it.value }
        elementsByTag.getOrPut(tag) { mutableListOf() }.add(HtmlElement(tag, text, fullText, attributes))

        // Рекурсивно обходим дочерние элементы
        element.children().forEach { child ->
            parseElement(child)
        }
        updateTagCheckboxesPanel()
    }

    private fun updateTagCheckboxesPanel() {
        tagCheckboxesPanel.removeAll()
        tagCheckboxesPanel.layout = GridLayout(0, 2)

        // Отделяем теги для автотестов и остальные теги
        val testingTags = elementsByTag.keys.filter { it in tagsForTesting }.sorted()
        val otherTags = elementsByTag.keys.filter { it !in tagsForTesting }.sorted()

        val orderedTags = if (filterCheckbox.isSelected) {
            // Если фильтр активирован, показываем сначала теги для автотестов
            testingTags + otherTags
        } else {
            // Иначе следуем обычному порядку сортировки
            (testingTags + otherTags).sorted()
        }

        // Добавляем чекбоксы для всех отфильтрованных и упорядоченных тегов
        orderedTags.forEach { tag ->
            val checkBox = JCheckBox(tag, false).apply {
                isEnabled = !filterCheckbox.isSelected || tag in tagsForTesting
                addActionListener { updateVisibleElements() }
            }
            tagCheckboxesPanel.add(checkBox)
        }

        tagCheckboxesPanel.revalidate()
        tagCheckboxesPanel.repaint()
    }

    private fun updateVisibleElements() {
        listModel.clear()
        elementsByTag.forEach { (tag, elements) ->
            val checkBox = tagCheckboxesPanel.components.find { it is JCheckBox && it.text == tag } as? JCheckBox
            if (checkBox?.isSelected == true) {
                elements.forEach { element ->
                    listModel.addElement(element)
                }
            }
        }
    }
}