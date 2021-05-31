package projeto12

import JSON
import JSONArray
import JSONObject
import SearchForKey
import SearchString
import projeto34.CreateTree

class Generator(private val o: Any?) { //pode ser ou nao null

    fun jSON(): String {
        val visitor = JSON()
        if (o is List<Any?>) {
            val array = JSONArray(o, null)
            array.rArray()
            array.accept(visitor)
        } else if (o is String || o is Int || o is Boolean || o is Double || o == null || o is Enum<*>) {
            null
        } else {
            val objeto = JSONObject(o, null)
            objeto.rObj()
            objeto.accept(visitor)
        }
        return visitor.texto
    }

    fun jSONGUI() {
        val visitor = CreateTree()
        if (o is List<Any?>) {
            val array = JSONArray(o, null) // primeiro item recebido
            array.rArray()
            visitor.open(array)
        } else if (o is String || o is Int || o is Boolean || o is Double || o == null || o is Enum<*>) {
            null
        } else {
            val objeto = JSONObject(o, null)
            objeto.rObj()
            visitor.open(objeto)
        }
    }


    fun searchStrings(): List<String> { // procura string.
        val visitor = SearchString()
        if (o is List<Any?>) {
            val array = JSONArray(o, null)
            array.rArray()
            array.accept(visitor)
        } else if (o is String || o is Int || o is Boolean || o is Double || o == null || o is Enum<*>) {
            null
        } else {
            val objeto = JSONObject(o, null)
            objeto.rObj()
            objeto.accept(visitor)
        }
        return visitor.lista
    }

    fun searchKey(key: String): List<Any?> {
        val visitor = SearchForKey(key)
        if (o is List<Any?>) {
            val array = JSONArray(o, null)
            array.rArray()
            array.accept(visitor)
        } else if (o is String || o is Int || o is Boolean || o is Double || o == null || o is Enum<*>) {
            null
        } else {
            val objeto = JSONObject(o, null)
            objeto.rObj()
            objeto.accept(visitor)
        }
        return visitor.lista
    }

}