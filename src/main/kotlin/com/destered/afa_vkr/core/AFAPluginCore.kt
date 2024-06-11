
import com.destered.afa_vkr.AppiumUtils
import com.destered.afa_vkr.UtilsFile
import com.destered.afa_vkr.dialog.AppiumCheckDialog
import com.destered.afa_vkr.dialog.EnterDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class AFAPluginCore : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        if (AppiumUtils.getAppium() == null) {
            val dialog = AppiumCheckDialog()
            if (dialog.showAndGet()) {
                // Пользователь выбрал продолжить работу
                showPluginUI(event)
            }
        } else {
            showPluginUI(event)
        }
    }

    private fun showPluginUI(event: AnActionEvent) {
        val dialog = EnterDialog()
        UtilsFile.project = event.project
        dialog.show()
    }
}