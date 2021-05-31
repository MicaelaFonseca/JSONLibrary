package projeto12

import JSONArray
import JSONObject
import Visitor

abstract class Element(val value: Any?) { //classe abstract que serve de modelo para criar outras classes

    abstract fun accept(v: Visitor)  //funçao accept que define os metodos comuns a todas as classes do Visitor

}