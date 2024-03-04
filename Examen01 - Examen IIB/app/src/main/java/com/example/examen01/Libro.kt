package com.example.examen01

import java.util.Date

class Libro {
    var idLibro: Int? = null
    var idAutor: Int? = null
    var titulo: String? = null
    var fechaPublicacion: String? = null
    var genero: String? = null
    var precio: Double? = null
    var bestSeller: Boolean? = null

    // Constructor sin argumentos requerido por Firebase
    constructor()

    // Constructor con argumentos para uso general
    constructor(idLibro: Int, idAutor: Int, titulo: String, fechaPublicacion: String, genero: String, precio: Double, bestSeller: Boolean) {
        this.idLibro = idLibro
        this.idAutor = idAutor
        this.titulo = titulo
        this.fechaPublicacion = fechaPublicacion
        this.genero = genero
        this.precio = precio
        this.bestSeller = bestSeller
    }
}
