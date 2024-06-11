
import com.destered.afa_vkr.model.HtmlElement
import javax.swing.JDialog
import javax.swing.JScrollPane
import javax.swing.JTextArea

class HtmlElementDetailsDialog(htmlElement: HtmlElement) : JDialog(null, "Предварительный просмотр выбранных элементов", ModalityType.APPLICATION_MODAL) { // Установка модальности
    init {
        var text = ""
        htmlElement.attributes.forEach {
            text += "${it.key} : ${it.value} \n"
        }

        val textArea = JTextArea(text).apply {
            isEditable = false
            wrapStyleWord = true
            lineWrap = true
        }
        this.add(JScrollPane(textArea))
        this.setSize(400, 300)
        this.setLocationRelativeTo(null) // Центрирование окна
    }
}