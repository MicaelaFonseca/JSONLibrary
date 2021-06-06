package projeto34

import Action
import FileTreeSkeleton
import JSON
import JSONArray
import JSONObject
import JSONVariable

import removeKey
import java.io.File

class Edit : Action { // Editar as propriedades de um objeto JSON

    override val name: String
        get() = "Editar" //texto do botão

    override val textBox: Boolean
        get() = true // tem ou não text bob

    override var input: String = ""

    override fun execute(window: FileTreeSkeleton) {

        if (window.selectedItem != null) {
            if(name != "") {
                window.selectedItem!!.text = "" //data
            }
        }

    }
}

class WriteJSON : Action { //Escrever o JSON visível na área da direita para um ficheiro

    override val name: String
        get() = "JSON" //texto do botão

    override val textBox: Boolean
        get() = false

    override var input: String = ""

    override fun execute(window: FileTreeSkeleton) {

        val wroteJSON = File("newJSON.json")
        if (window.selectedItem != null){
            val data = window.selectedItem!!.data
            var textoJSON = ""
            val serializationVisit = JSON()
            if (data is JSONObject) {
                val data = data
                data.accept(serializationVisit)
                textoJSON = removeKey(serializationVisit.texto, data.getKey()) // elimina excessos de string

            } else if (data is JSONArray) {
                val data = data
                data.accept(serializationVisit)
                textoJSON = removeKey(serializationVisit.texto, data.getKey())

            } else {
                val data = data as JSONVariable
                if (data.o is String || data.o is Enum<*>) {
                    textoJSON = "\"" + data.o.toString() + "\""
                } else {
                    textoJSON = data.o.toString()
                }
            }
            wroteJSON.writeText(textoJSON)
        }

    }
}