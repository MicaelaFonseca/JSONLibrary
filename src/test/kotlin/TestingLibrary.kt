import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestingLibrary {

    fun create(): Generator {
        val literatura = Literature("Desassossego", "Romance", "Fernando Pessoa")
        val instruments = mutableListOf<Any>()
        instruments.add("Piano")
        instruments.add("Orgao")
        val musica = Music(instruments, Gender.indie, "The Paper Kites", true)
        val persona = Person("Micaela", 22, "Auditor", "Systems Engineering", literatura, musica)
        return Generator(persona)
    }

    val trueJson = """{
"age":  22, 
"degree": "Systems Engineering", 
"job": "Auditor", 
"literatura": {
"book": "Desassossego", 
"gender": "Romance", 
"writer": "Fernando Pessoa"
},
"musica": {
"band": "The Paper Kites", 
"MusicalGender": "indie", 
"instruments": ["Piano", "Orgao"]
},
"name": "Micaela"
}"""

    @Test
    fun testSerialization() {
        val a = create()
        assertEquals(trueJson, a.jSON(), "Não são iguais")

    }

    val trueSearch =
        "[Systems Engineering, Auditor, Desassossego, Romance, Fernando Pessoa, The Paper Kites, indie, Piano, Orgao, Micaela]"

    @Test
    fun testSearch() {
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