package Repositorio

import Model.Autor
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class AutorRepository(private val filePath: String) {

    private val autores: MutableList<Autor> = mutableListOf()
    private val objectMapper = jacksonObjectMapper()
    private var lastAssignedId: Int = 0 // Inicializar en 0 o el último ID conocido


    init {
        loadDataFromFile()
    }

    //funcion para cargar o crear el archivo
    private fun loadDataFromFile() {
        try {
            val file = File(filePath)
            if (file.exists()) {
                val jsonContent = file.readText()
                autores.addAll(objectMapper.readValue(jsonContent))
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
            val jsonContent = objectMapper.writeValueAsString(autores)
            val file = File(filePath)
            file.writeText(jsonContent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isIdUnique(id: Int): Boolean {
        return autores.none { it.idAutor == id }
    }

    //Empieza el CRUD
    //Crear
    fun create(newAutor: Autor): Autor {
        println("Creando Autor")
        autores.add(newAutor)
        saveDataToFile()
        return newAutor
    }

    //Obtener todos los autores
    fun getAutores(): List<Autor> {
        println("Obteniendo autores")
        return autores.toList()
    }

    //obtener autor por identificador
    fun getAutorByIdentificador(identificador: Int): Autor?{
        println("Obteniendo autor")
        return autores.find { it.idAutor == identificador }
    }

    //Actualizar por el identificador
    fun updateByIdentificador(idAutorSeleccionado: Int, newAutor: Autor): Autor? {
        println("Actualizando datos del autor")
        var index = autores.indexOfFirst { it.idAutor == idAutorSeleccionado }
        if (index != -1) {
            autores[index] = newAutor
            saveDataToFile()
            return autores[index]
        }
        return null
    }

    //Eliminar al autor
    fun deleteById(idAutor: Int): Boolean {
        println("Borrando autor")
        val index = autores.indexOfFirst { it.idAutor == idAutor }
        return if (index != -1) {
            autores.removeAt(index)
            saveDataToFile()
            true
        } else {
            false
        }
    }

}