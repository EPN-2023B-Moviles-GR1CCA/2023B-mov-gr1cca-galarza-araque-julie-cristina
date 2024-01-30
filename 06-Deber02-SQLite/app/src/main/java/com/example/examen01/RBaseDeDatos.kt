package com.example.examen01

import Autor
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RBaseDeDatos (contexto: Context?): SQLiteOpenHelper(
    contexto,
    "Examen1",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {

        val scriptSQLCrearTablaAutor =
            """
                CREATE TABLE AUTOR(
                idAutor INTEGER PRIMARY KEY, 
                nombre VARCHAR(50),
                fechaNacimiento VARCHAR(50),
                nacionalidad VARCHAR(50),
                premioNobel VARCHAR(50) 
                )
            """.trimIndent()

        db?.execSQL(scriptSQLCrearTablaAutor)
        val scriptSQLCrearTablaLibro =
            """
                CREATE TABLE LIBRO(
                idLibro INTEGER PRIMARY KEY, 
                idAutor INTEGER,
                titulo VARCHAR(50),
                fechaPublicacion TEXT,
                genero VARCHAR(50),
                precio DOUBLE,
                bestSeller INTEGER 
                )
            """.trimIndent()

        db?.execSQL(scriptSQLCrearTablaLibro)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")

    }
    fun consultarAutorPorID(id: Int): Autor{
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM AUTOR WHERE idAutor = ?
        """.trimIndent()
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            parametrosConsultaLectura
        )
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        val usuarioEncontrado = Autor(0,"","","","")
        if(existeUsuario){
            do {
                val id = resultadoConsultaLectura.getInt(0)
                val nombre = resultadoConsultaLectura.getString(1)
                val fechaNacimiento = resultadoConsultaLectura.getString(2)
                val nacionalidad = resultadoConsultaLectura.getString(3)
                val premioNoberl = resultadoConsultaLectura.getString(4)
                if ( id != null){
                    usuarioEncontrado.idAutor = id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.fechaNacimiento = fechaNacimiento
                    usuarioEncontrado.nacionalidad = nacionalidad
                    usuarioEncontrado.premioNobel = premioNoberl
                }
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado
    }
    fun crearAutor(newAutor: Autor): Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("idAutor", newAutor.idAutor)
        valoresAGuardar.put("nombre", newAutor.nombre)
        valoresAGuardar.put("fechaNacimiento",newAutor.fechaNacimiento)
        valoresAGuardar.put("nacionalidad", newAutor.nacionalidad)
        valoresAGuardar.put("premioNobel", newAutor.premioNobel)

        val resultadoGuardar = basedatosEscritura
            .insert(
                "AUTOR",
                null,
                valoresAGuardar
            )
        basedatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }

    fun actualizarAutor(
        idAutor:Int,
        nombre: String,
        fechaNacimiento: String,
        nacionalidad: String,
        premioNoberl: String,

        ):Boolean{
        val conexionEscritura = writableDatabase
        val valoresAActualizarAutor = ContentValues()
        valoresAActualizarAutor.put("idAutor", idAutor)
        valoresAActualizarAutor.put("nombre", nombre)
        valoresAActualizarAutor.put("fechaNacimiento", fechaNacimiento)
        valoresAActualizarAutor.put("nacionalidad", nacionalidad)
        valoresAActualizarAutor.put("premioNobel", premioNoberl)

        val parametrosConsultaActualizarAutor = arrayOf( idAutor.toString())
        val resultadoActualización = conexionEscritura
            .update(
                "AUTOR",
                valoresAActualizarAutor,
                "idAutor=?",
                parametrosConsultaActualizarAutor
            )
        conexionEscritura.close()
        return if(resultadoActualización == -1) false else true
    }

    fun consultarAutores(): List<Autor> {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM AUTOR"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        val listaAutores = mutableListOf<Autor>()

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val idAutor = resultadoConsultaLectura.getInt(0)
                val nombre = resultadoConsultaLectura.getString(1)
                val fechaNacimiento = resultadoConsultaLectura.getString(2)
                val nacionalidad = resultadoConsultaLectura.getString(3)
                val premioNobel = resultadoConsultaLectura.getString(4) // Leer como cadena

                println("ID: $idAutor, Nombre: $nombre, Fecha Nacimiento: $fechaNacimiento, Nacionalidad: $nacionalidad, Premio Nobel: $premioNobel")



                val autor = Autor(idAutor, nombre, fechaNacimiento, nacionalidad, premioNobel)
                listaAutores.add(autor)
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()

        return listaAutores
    }

    fun eliminarAutor(idAutor: Int):Boolean{
        val conexionEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(idAutor.toString())
        val resultadoEliminación = conexionEscritura
            .delete(
                "AUTOR",
                "idAutor=?",
                parametrosConsultaDelete
            )
        conexionEscritura.close()
        return if(resultadoEliminación == -1 ) false else true
    }
    fun consultarLibroPorID(idLibro: Int, idAutor: Int): Libro {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
        SELECT * FROM LIBRO WHERE idLibro = ? AND idAutor = ?
     """.trimIndent()
        val parametrosConsultaLectura = arrayOf(idLibro.toString(), idAutor.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            parametrosConsultaLectura
        )
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        val libroEncontrado = Libro(0, 0, "", "", "", 0.0, false)
        if (existeUsuario) {
            do {
                val idLibro = resultadoConsultaLectura.getInt(0)
                val idAutorL = resultadoConsultaLectura.getInt(1)
                val titulo = resultadoConsultaLectura.getString(2)
                val fechaPublicacion = resultadoConsultaLectura.getString(3)
                val genero = resultadoConsultaLectura.getString(4)
                val precio = resultadoConsultaLectura.getDouble(5)
                val bestSeller = resultadoConsultaLectura.getInt(6) == 1

                libroEncontrado.idLibro = idLibro
                libroEncontrado.idAutor = idAutorL
                libroEncontrado.titulo = titulo
                libroEncontrado.fechaPublicacion = fechaPublicacion
                libroEncontrado.genero = genero
                libroEncontrado.precio = precio
                libroEncontrado.bestSeller = bestSeller

            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return libroEncontrado
    }


    fun crearLibro(newLibro: Libro): Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()

        valoresAGuardar.put("idLibro", newLibro.idLibro)
        valoresAGuardar.put("idAutor", newLibro.idAutor)
        valoresAGuardar.put("titulo", newLibro.titulo)
        valoresAGuardar.put("fechaPublicacion", newLibro.fechaPublicacion)
        valoresAGuardar.put("genero", newLibro.genero)
        valoresAGuardar.put("precio", newLibro.precio)
        valoresAGuardar.put("bestSeller", newLibro.bestSeller)

        val resultadoGuardar = basedatosEscritura
            .insert(
                "LIBRO",
                null,
                valoresAGuardar
            )
        basedatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }

    fun consultarLibros(): List<Libro> {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM LIBRO"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        val listaLibros = mutableListOf<Libro>()

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val idLibro = resultadoConsultaLectura.getInt(0)
                val idAutor = resultadoConsultaLectura.getInt(1)
                val titulo = resultadoConsultaLectura.getString(2)
                val fechaPublicacion = resultadoConsultaLectura.getString(3)
                val genero = resultadoConsultaLectura.getString(4)
                val precio = resultadoConsultaLectura.getDouble(5)
                val bestSeller = resultadoConsultaLectura.getInt(6) == 1 // Leer como entero y convertir a booleano

                println("ID Libro: $idLibro, ID Autor: $idAutor, Titulo: $titulo, Fecha Publicación: $fechaPublicacion, Genero: $genero, Precio: $precio, Best Seller: $bestSeller")

                val libro = Libro(idLibro, idAutor, titulo, fechaPublicacion, genero, precio, bestSeller)
                listaLibros.add(libro)
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()

        return listaLibros
    }
    fun consultarLibrosPorAutor(idAutor: Int): List<Libro> {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM LIBRO WHERE idAutor = ?"
        val parametrosConsultaLectura = arrayOf(idAutor.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        val listaLibros = mutableListOf<Libro>()

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val idLibro = resultadoConsultaLectura.getInt(0)
                val idAutor = resultadoConsultaLectura.getInt(1)
                val titulo = resultadoConsultaLectura.getString(2)
                val fechaPublicacion = resultadoConsultaLectura.getString(3)
                val genero = resultadoConsultaLectura.getString(4)
                val precio = resultadoConsultaLectura.getDouble(5)
                val bestSeller = resultadoConsultaLectura.getInt(6) == 1 // Leer como entero y convertir a booleano

                val libro = Libro(idLibro, idAutor, titulo, fechaPublicacion, genero, precio, bestSeller)
                listaLibros.add(libro)
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()

        return listaLibros
    }

    fun actualizarLibro(
        idLibro: Int,
        idAutor:Int,
        titulo: String,
        fechaPublicacion: String,
        genero: String,
        precio: Double,
        bestSeller: Boolean,

        ):Boolean{
        val conexionEscritura = writableDatabase
        val valoresAActualizarLibro = ContentValues()
        valoresAActualizarLibro .put("idLibro", idLibro)
        valoresAActualizarLibro .put("idAutor", idAutor)
        valoresAActualizarLibro .put("titulo", titulo)
        valoresAActualizarLibro .put("fechaPublicacion", fechaPublicacion)
        valoresAActualizarLibro .put("genero", genero)
        valoresAActualizarLibro .put("precio", precio)
        valoresAActualizarLibro .put("bestSeller", bestSeller)

        val parametrosConsultaActualizarLibro = arrayOf( idLibro.toString())
        val resultadoActualización = conexionEscritura
            .update(
                "LIBRO",
                valoresAActualizarLibro,
                "idLibro=?",
                parametrosConsultaActualizarLibro
            )
        conexionEscritura.close()
        return if(resultadoActualización == -1) false else true
    }
    fun eliminarLibro(idLibro: Int):Boolean{
        val conexionEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(idLibro.toString())
        val resultadoEliminación = conexionEscritura
            .delete(
                "LIBRO",
                "idLibro=?",
                parametrosConsultaDelete
            )
        conexionEscritura.close()
        return if(resultadoEliminación == -1 ) false else true
    }
}