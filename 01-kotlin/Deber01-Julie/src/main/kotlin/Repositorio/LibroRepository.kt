package Repositorio


import Model.Libro
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.awt.print.Book
import java.io.File

class LibroRepository (private val filePath: String){

    val libros: MutableList<Libro> = mutableListOf()
    private val objectMapper = jacksonObjectMapper()

    init {
        loadDataFromFile()
    }

    //funcion para cargar o crear el archivo
    private fun loadDataFromFile() {
        try {
            val file = File(filePath)
            if (file.exists()) {
                val jsonContent = file.readText()
                libros.addAll(objectMapper.readValue(jsonContent))
            } else {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Guardar los datos en el archivo
    private fun saveDataToFile() {
        try {
            val jsonContent = objectMapper.writeValueAsString(libros)
            val file = File(filePath)
            file.writeText(jsonContent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isBookIdUnique(idLibro: Int): Boolean {
        return libros.none { it.idLibro == idLibro }
    }

    //Empieza el CRUD
    //Crear
    fun create(newBook: Libro): Libro {
        println("Creando Libro")
        libros.add(newBook)
        saveDataToFile()
        return newBook
    }

    //Obtener todos los libros del autor
    fun getBooksByAutor(idAutor: Int): List<Libro> {
        println("Obteniendo libros")

        return libros.filter { it.idAutor == idAutor }
    }

    //obtener autor por identificador
    fun getBookByIdentificador(idLibro: Int): Libro?{
        println("Obteniendo libro")
        return libros.find { it.idLibro == idLibro }
    }

    fun getBookByIdentificadorAndAutor(idLibro: Int, idAutor: Int): Libro?{
        println("Obteniendo libro")
        return libros.find { it.idLibro == idLibro && it.idAutor == idAutor }
    }
    //Actualizar por el identificador
    fun updateByIdentificadorAndIdAutor(idLibroSeleccionado: Int, newLibro: Libro, idAutor: Int): Libro? {
        println("Actualizando datos del libro")
        var index = libros.indexOfFirst { it.idAutor == idAutor && it.idLibro == idLibroSeleccionado }
        if (index != -1) {
            libros[index] = newLibro
            saveDataToFile()
            return libros[index]
        }
        return null
    }

    //Eliminar al libro por el autor
    fun deleteByIdAndIdAutor(idAutor: Int, idLibro: Int) : Boolean{
        println("Borrando libro")
        var index = libros.indexOfFirst { it.idAutor == idAutor && it.idLibro == idLibro }
        if (index != -1) {
            libros.removeAt(index)
            saveDataToFile()
            return true
        }
        return false
    }
}
