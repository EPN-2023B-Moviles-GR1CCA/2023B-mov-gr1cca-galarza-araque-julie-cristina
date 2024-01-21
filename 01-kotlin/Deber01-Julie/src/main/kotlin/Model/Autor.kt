package Model

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.Date


class Autor(
    val idAutor: Int,
    var nombre: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" )
    var fechaNacimiento: Date,
    var nacionalidad: String,
    var premioNobel: Boolean
) {
    override fun toString(): String {
        return "Autor(idAutor=$idAutor, nombre='$nombre', fechaNacimiento=$fechaNacimiento, nacionalidad='$nacionalidad', premioNobel=$premioNobel)"
    }
}