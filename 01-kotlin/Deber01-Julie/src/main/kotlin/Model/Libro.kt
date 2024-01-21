package Model

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

class Libro (
    val idLibro: Int,
    val idAutor: Int,
    var titulo: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" )
    var fechaPublicacion: Date,
    var genero: String,
    var precio: Double,
    var bestSeller: Boolean

    ){

         init {
             if (precio > 0) this.precio

         }

    override fun toString(): String {
        return "Libro(idLibro=$idLibro, idAutor=$idAutor, titulo='$titulo', fechaPublicacion=$fechaPublicacion, genero='$genero', precio=$precio, bestSeller=$bestSeller)"
    }

}