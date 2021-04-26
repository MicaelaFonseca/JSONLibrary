import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestingLibrary {

    fun create(): Generator {
        val literatura = Literature("Lá, onde o vento chora", "Romance", "Fernando Pessoa")
        val instruments = mutableListOf<Any>()
        instruments.add("Piano")
        instruments.add("Orgao")
        val musica = Music(instruments, Gender.folk, "The Paper Kites", true)
        val persona = Person("Micaela", 22, "Auditor", "Systems Engineering", literatura, musica)
        return Generator(persona)
    }

    val trueJson = """{
"age":  22, 
"degree": "Systems Engineering", 
"job": "Auditor", 
"literatura": {
"book": "Lá, onde o vento chora", 
"gender": "Romance", 
"writer": "Fernando Pessoa"
},
"musica": {
"band": "The Paper Kites", 
"gender": "folk", 
"instruments": ["Piano", "Orgao"],
"play":  true
},
"name": "Micaela"
}"""

    @Test
    fun testSerialization() { // vai comparar o json criado com o true json
        val a = create()
        assertEquals(trueJson, a.jSON(), "Não são iguais")

    }

    val trueSearch =
        "[Systems Engineering, Auditor, Lá, onde o vento chora, Romance, Fernando Pessoa, The Paper Kites, folk, Piano, Orgao, Micaela]"

    @Test
    fun testSearch() { // vai comparar a string criada com a true searh
        val a = create()
        assertEquals(trueSearch, a.searchStrings().toString(), "Não são iguais")

    }

    val trueKey =
        "[Systems Engineering]"

    @Test
    fun testKey() {
        val a = create()
        assertEquals(trueKey, a.searchKey("degree").toString(), "Não são iguais")

    }

}