import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class JSONArray(val lista: List<Any?>, val parent: Element?) : Element(lista) { // (Leaf) //O valor pode ser null
    // percorre os filhos e guarda-os em memória
    var children = mutableListOf<Element>()
    var myName: String? = null
    fun rArray() {

        lista.forEach() {
            if (it is List<Any?>) {
                val jArray = JSONArray(it, this) //cria array
                jArray.rArray() //cria filhos do array
                children.add(jArray)
            } else if (it is String || it is Int || it is Boolean || it is Double || it == null || it is Enum<*>) {
                children.add(JSONVariable(it, this)) //
            } else {
                val jObject = JSONObject(it, this) //cria array
                jObject.rObj() //cria filhos do array
                children.add(JSONObject(it, this))
            }
        }
    }

    fun getName(): String? { // vai buscar nome da variavel
        if (parent is JSONObject) {
            parent.children.forEach() {
                if (it.value === this)
                    myName = it.key
            }
        }
        return myName
    }

    override fun accept(v: Visitor) {
        v.visitorJSONArray(this)
        children.forEach() {
            it.accept(v)
        }
        v.finalVisitorJSONArray(this)
    }
}

class JSONObject(val o: Any, val parent: Element?) : Element(o) { // (composite)  Pode ser array
    //Definir variaveis do objeto recebido como parametros; Coloca-se essa informacao na lista

    var myName: String? = null
    var children = mutableMapOf<String, Element>() // lista dos elementos do objeto

    fun rObj() {

        //reflexao para percorrer as propriedades da classe
        val clazz: KClass<Any> = o::class as KClass<Any>
        //parametros recebidos
        clazz.declaredMemberProperties.forEach() {
            val hasRemoveProp = it.hasAnnotation<RemoveProperty>()//verifica se tem anotacao
            val hasChangeProp = it.hasAnnotation<ChangeProperty>()
            if (!hasRemoveProp) {
                var nome = ""
                if (hasChangeProp) {
                    nome = it.findAnnotation<ChangeProperty>()!!.newName
                } else {
                    nome = it.name //Sempre string
                }
                var valor = it.call(o) //chama o valor da propriedade

                if (valor is List<*>) { // se o valor for do tipo lista - JSONArray
                    val jArray = JSONArray(valor, this) //cria array
                    jArray.rArray() //cria filhos do array
                    children[nome] = jArray
                } else if (valor is String || valor is Int || valor is Boolean || valor is Double || valor == null || valor is Enum<*>) { //se for tipo primitivo
                    val variable = JSONVariable(valor, this)
                    children[nome] = variable
                } else {
                    val jObject = JSONObject(valor, this) //cria array
                    jObject.rObj() //cria filhos do array
                    children[nome] = jObject
                }
            }
        }
    }

    fun getName(): String? { // vai buscar nome da variavel
        if (parent is JSONObject) {
            parent.children.forEach() {
                if (it.value === this)
                    myName = it.key
            }
        }
        return myName
    }

    override fun accept(v: Visitor) { // aceita o JSONObject (Composite) e percorre a lista de objetos
        v.visitorJSONObject(this)
        children.values.forEach() {
            it.accept(v)
        }
        v.finalVisitorJSONObject(this)
    }
}

class JSONVariable(val o: Any?, val parent: Element) : Element(o) { // Parent nao pode ser null
    var myName: String? = null

    fun getName(): String? { // vai buscar nome da variavel
        if (parent is JSONObject) {
            parent.children.forEach() {
                if (it.value === this)
                    myName = it.key
            }
        }
        return myName
    }

    override fun accept(v: Visitor) {
        v.visitorJSONVariable(this)
    }
}
