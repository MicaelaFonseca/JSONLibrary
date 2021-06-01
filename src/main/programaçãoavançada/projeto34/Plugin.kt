package projeto34

import FrameSetup
import JSONArray
import JSONObject
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.TreeItem


class Setup : FrameSetup { //Apresentação
    override val title: String
        get() = "Visualizador de JSON"
    override val layoutManager: GridLayout
        get() = GridLayout(2, true)
    override val width: Int
        get() = 550
    override val height: Int
        get() = 450
    override val folderIcon: String
        get() = "folder.png"
    override val fileIcon: String
        get() = "document.png"

    override fun setIcons(node: TreeItem, display: Display) {
        val iconePasta = Image(display, folderIcon)
        val iconeFicheiro = Image(display, fileIcon)

        node.items.forEach {
            if (it.data is JSONObject || it.data is JSONArray)
            {
                it.image = iconePasta
                setIcons(it, display)
            }
            else {
                it.image = iconeFicheiro
                setIcons(it, display)
            }
            setIcons(it, display)
        }
    }

    override fun addText(node: TreeItem, display: Display) {
        node.items.forEach {
            if (it.data is JSONArray) { //altera o nome das listas
                it.text = "Array"
                addText(it, display)
            }
            else if (it.data is JSONObject) {
                it.text = "Object"
                addText(it, display)
            }

        }
    }

    override fun excludeNode(node: TreeItem) {
        node.items.forEach {
            if (it.data is JSONArray) {
                it.items.forEach { child->
                    child.dispose()
                    excludeNode(it)
                }
                }
            else if (it.data is JSONObject) {
                excludeNode(it)
            }

        }
    }
}


class SetupInicial : FrameSetup { //Apresentação
    override val title: String
        get() = "Visualizador de JSON"
    override val layoutManager: GridLayout
        get() = GridLayout(2, true)
    override val width: Int
        get() = 550
    override val height: Int
        get() = 450
    override val folderIcon: String
        get() = "folder.png"
    override val fileIcon: String
        get() = "document.png"

    override fun setIcons(node: TreeItem, display: Display) {
        val iconePasta = Image(display, folderIcon)
        val iconeFicheiro = Image(display, fileIcon)

        node.items.forEach {
            if (it.data is JSONObject || it.data is JSONArray)
            {
                it.image = iconePasta
                setIcons(it, display)
            }
            else {
                it.image = iconeFicheiro
                setIcons(it, display)
            }
            setIcons(it, display)
        }
    }

}