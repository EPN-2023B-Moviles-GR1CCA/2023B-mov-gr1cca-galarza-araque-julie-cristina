package com.example.examen01

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AutorLibroFirestore {

    private val db = Firebase.firestore

    // CRUD Autores
    fun crearAutor(newAutor: Autor): Task<Void> {
        val documentReference = db.collection("autores").document(newAutor.idAutor.toString())
        return documentReference.set(newAutor)
    }

    fun obtenerAutores(callback: (ArrayList<Autor>) -> Unit) {
        val autoresList = ArrayList<Autor>()
        db.collection("autores")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val autor = document.toObject(Autor::class.java)
                    autor.idAutor = document.id.toIntOrNull()
                    autoresList.add(autor)
                }
                callback(autoresList)
            }
            .addOnFailureListener { exception ->
                // Manejar errores
            }
    }

    fun consultarAutorPorId(idAutor: String): Task<DocumentSnapshot> {
        val documentReference = db.collection("autores").document(idAutor)
        return documentReference.get()
    }

    fun actualizarAutorPorId(datosActualizados: Autor): Task<Void> {
        val documentReference = db.collection("autores").document(datosActualizados.idAutor.toString())
        return documentReference.set(datosActualizados)
    }

    fun eliminarAutorPorId(idAutor: String): Task<Void> {
        val documentReference = db.collection("autores").document(idAutor)
        return documentReference.delete()
    }

    // CRUD Libros
    fun obtenerLibrosPorAutor(idAutor: String, callback: (ArrayList<Libro>) -> Unit) {
        val libros = ArrayList<Libro>()
        db.collection("autores").document(idAutor).collection("libros")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val libro = document.toObject(Libro::class.java)
                    libro?.idLibro = document.id.toIntOrNull()
                    libro?.let { libros.add(it) }
                }
                callback(libros)
            }
            .addOnFailureListener { exception ->
                // Manejar errores
            }
    }

    fun crearLibro(idAutor: String, newLibro: Libro): Task<Void> {
        val autorDocumentReference = db.collection("autores").document(idAutor)
        return autorDocumentReference.collection("libros").document(newLibro.idLibro.toString()).set(newLibro)
    }

    fun consultarLibroPorId(idAutor: String, idLibro: String): Task<DocumentSnapshot> {
        return db.collection("autores").document(idAutor).collection("libros").document(idLibro).get()
    }

    fun actualizarLibroPorId(idAutor: String, datosActualizados: Libro): Task<Void> {
        return db.collection("autores").document(idAutor).collection("libros").document(datosActualizados.idLibro.toString()).set(datosActualizados)
    }

    fun eliminarLibroPorId(idAutor: String, idLibro: String): Task<Void> {
        return db.collection("autores").document(idAutor).collection("libros").document(idLibro).delete()
    }
}