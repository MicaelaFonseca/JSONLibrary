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
    val firstNodeName: String
    fun setIcons(node: TreeItem, display: Display)
    fun addText(node: TreeItem, display: Display)
    fun excludeNode(node: TreeItem)
}

class FileTreeSkeleton() {
    val shell: Shell
    val tree: Tree

    init {
        shell = Shell(Display.getDefault())
        shell.setSize(625, 500)
        shell.text = "Visualizador de JSON"
        shell.layout = GridLayout(2,false)

        tree = Tree(shell, SWT.SINGLE or SWT.BORDER)

        val labelJSON = Label(shell, SWT.NONE)

        fun searchText(node: TreeItem, pesquisa: String) { //procura o texto
            node.items.forEach() {
                if (it.text.contains(pesquisa)) {
                    it.background = Color(Display.getCurrent(),212,198,176)
                }
                if(it.data is JSONObject || it.data is JSONArray ) {
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
        val textBox = Text(shell,SWT.NONE) //Box para pesquisa
        textBox.addModifyListener{
            val text = textBox.text
            clearHighlight(tree.selection.first())
            searchText(tree.selection.first(), text)
        }


        fun removeKey (text : String, key: String?) : String{ //elimina excessos da string
            var textJSON = ""

            if( key != null ) {
                val tamanho = key.length
                val removeChars = tamanho + 3
                textJSON = text.drop(removeChars)
                textJSON = textJSON.dropLast(2) //elimina a ,
                return textJSON
            } else {
                return text
            }
        }

        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
            var textoJSON = ""
            val serializationVisit = JSON()
                if (tree.selection.first().data is JSONObject) {
                    val data = tree.selection.first().data as JSONObject
                    data.accept(serializationVisit)
                    textoJSON = removeKey(serializationVisit.texto, data.getKey())
                    labelJSON.text = textoJSON
                }
                else if( tree.selection.first().data is JSONArray ) {
                    val data = tree.selection.first().data as JSONArray
                    data.accept(serializationVisit)
                    textoJSON = removeKey(serializationVisit.texto, data.getKey())
                    labelJSON.text = textoJSON
                }
                else {
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
        if(parentItem == null) 0
        else 1 + parentItem.depth()


    fun open() {
        tree.expandAll() // abre a arvore toda
      //  shell.pack()
        shell.open()
        val display = Display.getDefault()
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        display.dispose()
    }
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


