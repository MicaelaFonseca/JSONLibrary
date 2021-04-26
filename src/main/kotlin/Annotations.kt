@Target(AnnotationTarget.PROPERTY)
annotation class RemoveProperty() // anotacao que remove uma propriedade ( neste caso, play)

@Target(AnnotationTarget.PROPERTY)
annotation class ChangeProperty(val newName : String) // anotacao que muda o nome da propriedade ( neste caso, de gender )
