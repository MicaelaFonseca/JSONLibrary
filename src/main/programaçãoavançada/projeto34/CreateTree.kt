package projeto34

import FileTreeSkeleton
import JSONArray
import JSONObject
import JSONVariable
import Visitor
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem
import projeto12.Element

class CreateTree : Visitor {

    lateinit var tree: Tree
    var currentParent: TreeItem? = null

    fun open(el: Element) {
        var window = Injector.create(FileTreeSkeleton::class)

        tree = window.tree //raiz da arvore
        el.accept(this) // inicia o visitor
        window.insertIcon()
        window.addText()
        window.excludeNode()
        window.open()
    }

    override fun visitorJSONArray(jArray: JSONArray) {

        if (jArray.parent == null) { // se parent null
            val node = TreeItem(tree, SWT.NONE)
            node.text = "Array" // chave denomina-se array
            node.data = jArray
            currentParent = node // o parent é o próprio jarray
        } else {
            val node = TreeItem(currentParent, SWT.NONE) //assume o nó como pai
            node.text = jArray.getKey() // chave
            node.data = jArray
            currentParent = node // o parent é o próprio jarray
        }

    }

    override fun finalVisitorJSONArray(JArray: JSONArray) {
        currentParent = currentParent!!.parentItem // currentParent deixa de ser o próprio Array
    }

    override fun visitorJSONObject(jObj: JSONObject) {

        if (jObj.parent == null) {
            val node = TreeItem(tree, SWT.NONE)
            node.text = "Object"
            node.data = jObj
            currentParent = node
        } else {
            val node = TreeItem(currentParent, SWT.NONE)
            node.text = jObj.getKey()
            node.data = jObj
            currentParent = node
        }
    }

    override fun finalVisitorJSONObject(jObj: JSONObject) {
            currentParent = currentParent!!.parentItem
    }

    override fun visitorJSONVariable(jVariable: JSONVariable) {
        val node = TreeItem(currentParent, SWT.NONE)
        if (jVariable.getKey() != null) { // quando o parent é objeto
            if(jVariable.o is String || jVariable.o is Enum<*> ) {
            node.text = jVariable.getKey() + " : \"" + jVariable.o.toString() + "\""
            } else {   node.text = jVariable.getKey() + " : " + jVariable.o.toString() }
            node.data = jVariable
        }

        else { // quando o parent é array
            if(jVariable.o is String || jVariable.o is Enum<*> ) {
                node.text = " \"" + jVariable.o.toString() + "\""
            } else {   node.text = jVariable.o.toString() }
            node.data = jVariable
        }


    }


}