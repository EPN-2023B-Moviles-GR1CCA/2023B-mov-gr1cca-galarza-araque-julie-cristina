import java.util.Date

fun main(){
    println("Hola mundo")
    //INMUTABLEES ( NO se reasignan "=")
    val inmutable: String = "Adrian";
    //inmutable = "Vicente";

    //Mutable (Re asignar)
    var mutable: String = "Vecente";
    mutable = "Adrian";

    //Variable primitiva
    val nombreProfesor: String="Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true
    //Clases Java
    val fechaNacimiento: Date = Date()

    //SWITCH
    val estadoCivilWhen = "C"
    when (estadoCivilWhen){
        ("C") -> {
            println("Casado")
        }
        "S" ->{
            println("Soltero")
        }
        else ->{
            println("No sabemos")
        }
    }
    val coqueteo = if(estadoCivilWhen == "Si") else "No"
}