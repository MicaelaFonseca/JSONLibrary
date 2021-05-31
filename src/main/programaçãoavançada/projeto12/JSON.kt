import projeto12.Element
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class JSONArray(val lista: List<Any?>, val parent: Element?) : Element(lista) {
    // percorre os filhos e guarda-os em memória
    var children = mutableListOf<Element>()
    var myName: String? = null
    fun rArray() { //readArray

        lista.forEach() {
            if (it is List<Any?>) {
                val jArray = JSONArray(it, this)
                jArray.rArray()
                children.add(jArray)
            } else if (it is String || it is Int || it is Boolean || it is Double || it == null || it is Enum<*>) {
                children.add(JSONVariable(it, this))
            } else {
                val jObject = JSONObject(it, this)
                jObject.rObj()
                children.add(JSONObject(it, this))
            }
        }
    }

    fun getKey(): String? { // vai buscar a key da variavel
        if (parent is JSONObject) {
            parent.children.forEach() /* percorre cada filho do pai*/ {
                if (it.value === this)
                    myName = it.key
            }
        }
        return myName
    }

    override fun accept(v: Visitor) {
        v.visitorJSONArray(this)
        children.forEach() {
            it.accept(v) // passa pela estrutura e imprime todos os elementos
        }
        v.finalVisitorJSONArray(this)
    }
}

// object que contem outros objetos - composite
class JSONObject(val o: Any, val parent: Element?) : Element(o) {

    var myName: String? = null
    var children = mutableMapOf<String, Element>() // map dos elementos do objeto

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

                if (valor is List<*>) {
                    val jArray = JSONArray(valor, this)
                    jArray.rArray()
                    children[nome] = jArray
                } else if (valor is String || valor is Int || valor is Boolean || valor is Double || valor == null || valor is Enum<*>) {
                    val variable = JSONVariable(valor, this)
                    children[nome] = variable
                } else {
                    val jObject = JSONObject(valor, this)
                    jObject.rObj()
                    children[nome] = jObject
                }
            }
        }
    }

    fun getKey(): String? { // vai buscar a key da variavel
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

class JSONVariable(val o: Any?, val parent: Element) : Element(o) {
    var myName: String? = null

    fun getKey(): String? {
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
