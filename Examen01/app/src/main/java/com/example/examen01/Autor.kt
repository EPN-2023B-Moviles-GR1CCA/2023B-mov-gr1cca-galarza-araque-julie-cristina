import android.database.Cursor
import java.util.Date

class Autor(
    var idAutor: Int?,
    var nombre: String?,
    var fechaNacimiento: String?,
    var nacionalidad: String?,
    var premioNobel: String?
){

    override fun toString(): String {

        return """
        Autor Details:
        ID: $idAutor
        Nombre del Autor: $nombre
        Fecha de Nacimiento: $fechaNacimiento
        Nacionalidad: $nacionalidad
        Premio Nobel: $premioNobel
    """.trimIndent()
    }


}
