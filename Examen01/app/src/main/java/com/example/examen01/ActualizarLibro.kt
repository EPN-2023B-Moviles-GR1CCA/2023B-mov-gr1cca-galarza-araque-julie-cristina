package com.example.examen01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.examen01.R
import com.google.android.material.snackbar.Snackbar


class ActualizarLibro : AppCompatActivity() {
    private lateinit var autorLibroFirestore: AutorLibroFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_libro)

        val idLibro = intent.extras?.getString("idLibro") ?: ""
        val idAutor = intent.extras?.getString("idAutor") ?: ""

        // Cargar la información existente del libro
        cargarInfoLibro(idAutor, idLibro)
        // Inicializa tus vistas aquí

        val botonActualizarLibro = findViewById<Button>(R.id.btn_actualizar_libro)
        botonActualizarLibro.setOnClickListener {
            val nuevoTitulo = findViewById<EditText>(R.id.input_titulo).text.toString()
            val nuevaFechaPublicacion = findViewById<EditText>(R.id.input_fechaPublicacion).text.toString()
            val nuevoGenero = findViewById<EditText>(R.id.input_genero).text.toString()
            val nuevoPrecio = findViewById<EditText>(R.id.input_precio).text.toString().toDouble()
            val esBestSeller = findViewById<EditText>(R.id.input_bestSeller).text.toString().toBoolean()

            // Crear objeto Libro actualizado
            val libroActualizado = Libro(
                idLibro = idLibro.toInt(),
                idAutor = idAutor.toInt(), // Asume que tienes esta propiedad en tu clase Libro
                titulo = nuevoTitulo,
                fechaPublicacion = nuevaFechaPublicacion,
                genero = nuevoGenero,
                precio = nuevoPrecio,
                bestSeller = esBestSeller
            )

            // Llamar a Firestore para actualizar
            autorLibroFirestore.actualizarLibroPorId(idAutor, libroActualizado).addOnSuccessListener {
                Toast.makeText(this, "Libro actualizado correctamente", Toast.LENGTH_LONG).show()
                // Puedes regresar a la actividad anterior o actualizar la UI aquí
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar el libro: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun cargarInfoLibro(idAutor: String, idLibro: String) {
        autorLibroFirestore.consultarLibroPorId(idAutor, idLibro).addOnSuccessListener { documento ->
            if (documento.exists()) {
                val libro = documento.toObject(Libro::class.java)
                findViewById<EditText>(R.id.input_titulo).setText(libro?.titulo)
                findViewById<EditText>(R.id.input_fechaPublicacion).setText(libro?.fechaPublicacion)
                findViewById<EditText>(R.id.input_genero).setText(libro?.genero)
                findViewById<EditText>(R.id.input_precio).setText(libro?.precio.toString())
                findViewById<EditText>(R.id.input_bestSeller).setText(libro?.bestSeller.toString())
            } else {
                mostrarSnackbar("Libro no encontrado")
            }
        }.addOnFailureListener {
            mostrarSnackbar("Error al cargar información del libro")
        }
    }

    private fun mostrarSnackbar(mensaje: String) {
        Snackbar.make(findViewById(R.id.id_layout_actualizar_libro), mensaje, Snackbar.LENGTH_LONG).show()
    }
}
