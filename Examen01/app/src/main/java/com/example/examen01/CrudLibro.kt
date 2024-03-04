package com.example.examen01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.examen01.R
import com.google.android.material.snackbar.Snackbar

class CrudLibro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_libro)

        val autorLibroFirestore = AutorLibroFirestore()
        val idAutor = intent.extras?.getString("idAutor")

        val btnGuardarLibro = findViewById<Button>(R.id.btn_crearLibro)
        btnGuardarLibro.setOnClickListener {
            try {
                val idLibro = findViewById<EditText>(R.id.input_idLibro).text.toString().toInt()
                val titulo = findViewById<EditText>(R.id.input_creartitulo).text.toString()
                val fechaPublicacion = findViewById<EditText>(R.id.input_crearFechaPublicacion).text.toString()
                val genero = findViewById<EditText>(R.id.input_crearGenero).text.toString()
                val precio = findViewById<EditText>(R.id.input_crearPrecio).text.toString().toDouble()
                val bestSeller = findViewById<EditText>(R.id.input_crearBestSeller).text.toString().toBoolean()

                // Validación del formato de fecha y otros datos si es necesario
                if (validarFormatoFecha(fechaPublicacion)) {
                    // Aquí se maneja el caso de que idAutor sea null
                    idAutor?.toIntOrNull()?.let { idAutorInt ->
                        val newLibro = Libro(idLibro, idAutorInt, titulo, fechaPublicacion, genero, precio, bestSeller)
                        autorLibroFirestore.crearLibro(idAutor, newLibro).addOnSuccessListener {
                            mostrarSnackbar("Libro creado exitosamente")
                            finish()
                        }.addOnFailureListener { e ->
                            mostrarSnackbar("Error al crear el libro")
                        }
                    } ?: mostrarSnackbar("ID de autor no es válido o está vacío")
                } else {
                    mostrarSnackbar("El formato de la fecha es incorrecto")
                }
            } catch (e: Exception) {
                mostrarSnackbar("Error al crear el libro: ${e.message}")
            }
        }
    }

    private fun mostrarSnackbar(mensaje: String) {
        Snackbar.make(findViewById(R.id.id_layout_libro), mensaje, Snackbar.LENGTH_LONG).show()
    }

    private fun validarFormatoFecha(fecha: String): Boolean {
        val regex = Regex("""^(\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$""")
        return regex.matches(fecha)
    }
}
