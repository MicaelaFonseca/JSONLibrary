import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*
import JSON
import org.eclipse.swt.graphics.Color
import java.awt.TextArea

/*fun main() {
  FileTreeSkeleton().open()
}*/

//data class Dummy(val number: Int)

interface FrameSetup {
    val title: String
    val layoutManager: GridLayout
    val width: Int
    val height: Int
    val folderIcon: String
    val fileIcon: String
    fun setIcons(node: TreeItem, display: Display) { }
    fun addText(node: TreeItem, display: Display) { }
    fun excludeNode(node: TreeItem) { }
}

interface Action {
    val name: String // nome do button
    val textBox: Boolean
    var input: String
    fun execute(window: FileTreeSkeleton)
}

class FileTreeSkeleton() {
    val shell: Shell
    val tree: Tree
    var selectedItem: TreeItem? = null
    @Inject
    private lateinit var setup: FrameSetup // Fase 4

    @InjectAdd
    private lateinit var actions : MutableList<Action>

    init {
        shell = Shell(Display.getDefault())
        shell.setSize(625, 500)
        shell.text = "Visualizador de JSON"
        shell.layout = GridLayout(2, false)

        tree = Tree(shell, SWT.SINGLE or SWT.BORDER)

        val labelJSON = Label(shell, SWT.NONE)

        fun searchText(node: TreeItem, pesquisa: String) { //procura o texto
            node.items.forEach() {
                if (it.text.contains(pesquisa)) {
                    it.background = Color(Display.getCurrent(), 212, 198, 176)
                }
                if (it.data is JSONObject || it.data is JSONArray) {
                    searchText(it, pesquisa)

                }

            }

        }

        fun clearHighlight(node: TreeItem) { // apaga os highlights
            node.items.forEach() {
                it.background = null

                if (it.data is JSONObject || it.data is JSONArray) {
                    clearHighlight(it)

                }
            }
        }

        val textBox = Text(shell, SWT.NONE) //Box para pesquisa
        textBox.addModifyListener {
            val text = textBox.text
            clearHighlight(tree.topItem)
            searchText(tree.topItem, text)
        }




        tree.addSelectionListener(object : SelectionAdapter() { //quando se carrega num nó
            override fun widgetSelected(e: SelectionEvent) {
                selectedItem = tree.selection.first()
                var textoJSON = ""
                val serializationVisit = JSON()
                if (tree.selection.first().data is JSONObject) {
                    val data = tree.selection.first().data as JSONObject
                    data.accept(serializationVisit)
                    textoJSON = removeKey(serializationVisit.texto, data.getKey())
                    labelJSON.text = textoJSON
                } else if (tree.selection.first().data is JSONArray) {
                    val data = tree.selection.first().data as JSONArray
                    data.accept(serializationVisit)
                    textoJSON = removeKey(serializationVisit.texto, data.getKey())
                    labelJSON.text = textoJSON
                } else {
                    val data = tree.selection.first().data as JSONVariable
                    if (data.o is String || data.o is Enum<*>) {
                        labelJSON.text = "\"" + data.o.toString() + "\""
                    } else {
                        labelJSON.text = data.o.toString()
                    }
                }
                labelJSON.requestLayout()
                println("selected: " + tree.selection.first().data)
            }
        })


//        val button = Button(shell, SWT.PUSH)
//        button.text = "depth"
//        button.addSelectionListener(object: SelectionAdapter() {
//            override fun widgetSelected(e: SelectionEvent) {
//                val item = tree.selection.first()
//                label.text = item.depth().toString()
//            }
//        })
    }

    // auxiliar para profundidade do nó
    fun TreeItem.depth(): Int =
        if (parentItem == null) 0
        else 1 + parentItem.depth()

    private fun setActions() {

        actions.forEach { action ->

            var inputTextBox: Text? = null
            if(action.textBox) {
                inputTextBox = Text(shell, SWT.BORDER) // cria text box
            }

            if(action.name != ""){ // se tiver button

                val button = Button(shell, SWT.PUSH)
                button.text = action.name

                button.addSelectionListener(object : SelectionAdapter() {
                    override fun widgetSelected(e: SelectionEvent?) {
                        super.widgetSelected(e)
                        if (inputTextBox != null) {
                            action.input = inputTextBox.text
                        }
                        action.execute(this@FileTreeSkeleton)
                    }
                })
            }
        }
    }

    fun open() {

        setActions()
        tree.expandAll() // abre a arvore toda
        //  shell.pack()
        shell.open()
        val display = Display.getDefault()
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        display.dispose()
    }


// auxiliares para varrer a árvore

    fun Tree.expandAll() = traverse { it.expanded = true }

    fun Tree.traverse(visitor: (TreeItem) -> Unit) {
        fun TreeItem.traverse() {
            visitor(this)
            items.forEach {
                it.traverse()
            }
        }
        items.forEach { it.traverse() }
    }

    fun insertIcon() {
        tree.items.forEach {
            setup.setIcons(it, Display.getDefault())
        }
    }
    fun addText() {
        tree.items.forEach {
            setup.addText(it, Display.getDefault())
        }
    }

    fun excludeNode() {
        tree.items.forEach {
            setup.excludeNode(it)
        }
    }

}

fun removeKey(text: String, key: String?): String { //elimina excessos da string
    var textJSON = ""

    if (key != null) {
        val tamanho = key.length
        val removeChars = tamanho + 3
        textJSON = text.drop(removeChars)
        textJSON = textJSON.dropLast(2) //elimina a ,
        return textJSON
    } else {
        return text
    }
}



