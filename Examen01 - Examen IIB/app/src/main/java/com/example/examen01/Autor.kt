package com.example.examen01

import android.database.Cursor
import java.util.Date

class Autor {
    var idAutor: Int? = null
    var nombre: String? = null
    var fechaNacimiento: String? = null
    var nacionalidad: String? = null
    var premioNobel: String? = null

    // Constructor sin argumentos requerido por Firebase
    constructor()

    // Constructor con argumentos para uso general
    constructor(idAutor: Int, nombre: String, fechaNacimiento: String, nacionalidad: String, premioNobel: String) {
        this.idAutor = idAutor
        this.nombre = nombre
        this.fechaNacimiento = fechaNacimiento
        this.nacionalidad = nacionalidad
        this.premioNobel = premioNobel
    }
}
