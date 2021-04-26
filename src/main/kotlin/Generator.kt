import java.util.*

class Generator(private val o: Any?) { //pode ser ou nao null

    fun jSON(): String {
        val visitor = JSON()
        if (o is List<Any?>) {
            val array = JSONArray(o, null)
            array.rArray()
            array.accept(visitor)

        } else if (o == null) {
            null
        } else {
            val objeto = JSONObject(o, null)
            objeto.rObj()
            objeto.accept(visitor)
        }
        return visitor.texto
    }

    fun searchStrings(): List<String> { // procura string.
        val visitor = SearchString()
        if (o is List<Any?>) { // se o parametro passado for lista, array.
            val array = JSONArray(o, null)
            array.rArray()
            array.accept(visitor)
        } else if (o == null) {
            null
        } else { // se o parametro passado for objeto, JSONObject
            val objeto = JSONObject(o, null)
            objeto.rObj()
            objeto.accept(visitor)
        }
        return visitor.lista // retorna a lista
    }

    fun searchKey(key: String): List<Any?> {
        val visitor = SearchForKey(key)
        if (o is List<Any?>) {
            val array = JSONArray(o, null)
            array.rArray()
            array.accept(visitor)
        } else if (o == null) {
            null
        } else {
            val objeto = JSONObject(o, null)
            objeto.rObj()
            objeto.accept(visitor)
        }
        return visitor.lista
    }

}