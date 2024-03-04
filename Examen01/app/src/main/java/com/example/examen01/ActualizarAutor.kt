package com.example.examen01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class ActualizarAutor : AppCompatActivity() {
    private lateinit var autorLibroFirestore: AutorLibroFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_autor)

        autorLibroFirestore = AutorLibroFirestore()

        val idAutor = intent.extras?.getString("idAutor") ?: return

        // Cargar la información existente del autor
        cargarInfoAutor(idAutor)

        val botonActualizarAutor = findViewById<Button>(R.id.btn_actualizarAutor)
        botonActualizarAutor.setOnClickListener {
            actualizarInfoAutor(idAutor)
        }
    }

    private fun cargarInfoAutor(idAutor: String) {
        autorLibroFirestore.consultarAutorPorId(idAutor).addOnSuccessListener { documento ->
            if (documento.exists()) {
                val autor = documento.toObject(Autor::class.java)
                findViewById<EditText>(R.id.input_actuNombreAutor).setText(autor?.nombre)
                findViewById<EditText>(R.id.input_actuFechaNacimiento).setText(autor?.fechaNacimiento)
                findViewById<EditText>(R.id.input_actuNacionalidad).setText(autor?.nacionalidad)
                findViewById<EditText>(R.id.input_actualizarPremioNovel).setText(autor?.premioNobel)
            } else {
                mostrarSnackbar("Autor no encontrado")
            }
        }.addOnFailureListener {
            mostrarSnackbar("Error al cargar información del autor")
        }
    }

    private fun actualizarInfoAutor(idAutor: String) {
        val nombre = findViewById<EditText>(R.id.input_actuNombreAutor).text.toString()
        val fechaNacimiento = findViewById<EditText>(R.id.input_actuFechaNacimiento).text.toString()
        val nacionalidad = findViewById<EditText>(R.id.input_actuNacionalidad).text.toString()
        val premioNobel = findViewById<EditText>(R.id.input_actualizarPremioNovel).text.toString()

        val autorActualizado = Autor(idAutor.toInt(), nombre, fechaNacimiento, nacionalidad, premioNobel)

        autorLibroFirestore.actualizarAutorPorId(autorActualizado).addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                mostrarSnackbar("Autor actualizado con éxito")
            } else {
                mostrarSnackbar("Error al actualizar autor")
            }
        }
    }

    private fun mostrarSnackbar(mensaje: String) {
        Snackbar.make(findViewById(R.id.id_layout_actualizar_libro), mensaje, Snackbar.LENGTH_LONG).show()
    }
}