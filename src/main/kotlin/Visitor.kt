import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

interface Visitor { //interface com funções que devem ser implementadas

    fun visitorJSONArray(node: JSONArray) {}
    fun finalVisitorJSONArray(node: JSONArray) {} // serve para fechar o array
    fun visitorJSONObject(node: JSONObject) {}
    fun finalVisitorJSONObject(node: JSONObject) {} // serve para fechar o objeto
    fun visitorJSONVariable(node: JSONVariable) {}
}

class JSON : Visitor {

    var texto = ""

    override fun visitorJSONArray(jArray: JSONArray) {
        val nome = jArray.getName() // vai buscar o nome do parametro

        texto += if (nome != null) "\"" + nome + "\": " + "[" else "[" // o nome pode vir null no caso de se tratar de uma variavel primitiva
    }

    override fun finalVisitorJSONArray(jArray: JSONArray) {
        val size = texto.length

        texto = texto.removeRange(size - 2, size)
        if (jArray.parent is JSONObject) texto += "]," + "\n" // se o parent for object, quer dizer que ainda há mais parametros a definir no resto da string e não pode levar ","
        else texto += "]"

    }

    override fun visitorJSONObject(jObj: JSONObject) {

        val nome = jObj.getName()

        texto += if (nome != null) "\"" + nome + "\": " + "{" + "\n" else "{" + "\n" // o nome pode vir null no caso de se tratar de uma variavel primitiva
    }

    override fun finalVisitorJSONObject(jObj: JSONObject) {
        val size = texto.length
        val aux = texto.get(size - 3) // vai medir a string lida de forma a eliminar a "," a mais
        if (aux.equals(']')) {
            texto = texto.removeRange(
                size - 2,
                size
            ) // caso a posição -3 tiver um "]", quer dizer que devemos eliminar a posicao -2 onde está a ","
        } else {
            texto = texto.removeRange(
                size - 3,
                size
            ) // caso a posicao -3 nao tiver um "]", quer dizer que devemos eliminar essa mesma posicao porque corresponde à ","
        }

        texto += "\n" + "}"
        if (jObj.parent is JSONObject) texto += "," + "\n"
    }


    override fun visitorJSONVariable(jVariable: JSONVariable) {

        val nome = jVariable.getName()

        if (nome != null) {
            texto += "\"" + nome + "\": "
        }

        val variavel = jVariable.o

        if (variavel is String) {
            texto += "\"" + variavel + "\"" + ", "
        } else if (variavel is Boolean || variavel is Int || variavel is Double || variavel == null) {
            texto += " " + variavel + ", "
        } else if (variavel is Enum<*>) {
            texto += "\"" + variavel.toString() + "\"" + ", "
        }
        if (jVariable.parent is JSONObject) texto += "\n" // caso o parent seja object ainda há mais propriedades para a string, portanto faz-se paragrafo

    }

}

class SearchString : Visitor { // class para o teste da pesquisa (string)

    val lista = mutableListOf<String>()

    override fun visitorJSONVariable(node: JSONVariable) {
        if (node.o is String) { // se o parametro for string, addiciona à lista
            lista.add(node.o)
        } else if (node.o is Enum<*>) { // se o parametro for Enum, converte o valor em string e adiciona à lista
            lista.add(node.o.toString())
        }
    }

}

class SearchForKey(val key: String) : Visitor { // class para o teste da pesquisa ( valor )

    val lista = mutableListOf<Any?>()

    override fun visitorJSONArray(node: JSONArray) {
        if (node.getName() == key) lista.add(node.lista)
    }

    override fun visitorJSONObject(node: JSONObject) {
        if (node.getName() == key) lista.add(node.o)
    }

    override fun visitorJSONVariable(node: JSONVariable) {
        if (node.getName() == key) lista.add(node.o)
    }

}
