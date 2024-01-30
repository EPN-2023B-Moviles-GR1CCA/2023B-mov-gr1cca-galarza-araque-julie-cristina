package com.example.examen01

import java.util.Date

class Libro(
    var idLibro: Int?,
    var idAutor: Int?,
    var titulo: String?,
    var fechaPublicacion: String?,
    var genero: String?,
    var precio: Double?,
    var bestSeller: Boolean?
) {

    override fun toString(): String {

        return """
        Libro Details:
        ID: $idLibro
        ID Autor: $idAutor
        Título del libro: $titulo
        Fecha de Publicación: $fechaPublicacion
        Genero: $genero
        Precio: $precio
        Best Seller: $bestSeller
    """.trimIndent()
    }


}