import projeto12.Generator

data class Person(
    val name: String,
    val age: Int,
    val job: String,
    val degree: String,
    val literatura: Literature,
    val musica: Music
)

data class Music(
    val instruments: MutableList<Any> = mutableListOf(),
    @ChangeProperty("MusicalGender")
    val gender: Gender,
    val band: String,
    @RemoveProperty
    val play: Boolean

)

data class Literature(
    val book: String,
    val gender: String,
    val writer: String
)

enum class Gender {
    rock, indie, folk
}

fun main() {
    val literatura = Literature("Desassossego", "Romance", "Fernando Pessoa")
    val instruments = mutableListOf<Any>()
    instruments.add("Piano")
    instruments.add("Orgao")
    val musica = Music(instruments, Gender.indie, "The Paper Kites",true )
    val persona = Person("Micaela", 22, "Auditor", "Systems Engineering", literatura, musica)


   /* listaTeste2.add("happy")
    listaTeste.add(2)
    listaTeste.add("sad")
    listaTeste.add(null)
    listaTeste.add(listaTeste2)


    */println("\n*****Solucao*****")
    val final = Generator(persona)
    println(final.jSON())
    println(final.searchStrings())
    println(final.searchKey("degree"))

}