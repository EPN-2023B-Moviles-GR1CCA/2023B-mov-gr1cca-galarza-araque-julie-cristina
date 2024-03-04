package com.example.examen01

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class CrudAutor2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_autor2)

        val db = AutorLibroFirestore()

        val btnVolverMain = findViewById<Button>(R.id.btn_volverMain2)
        btnVolverMain.setOnClickListener {
            finish()
        }

        val btnGuardarAutor = findViewById<Button>(R.id.btn_crearAutor2)
        btnGuardarAutor.setOnClickListener {
            try {
                val idAutor = findViewById<EditText>(R.id.input_crearIdAutor).text.toString().toInt()
                val nombre = findViewById<EditText>(R.id.input_crearNombreAutor).text.toString()
                val nacimiento = findViewById<EditText>(R.id.input_crearNacimiento).text.toString()
                val nacionalidad = findViewById<EditText>(R.id.input_crearNacionalidadAutor).text.toString()
                val premioNobel = findViewById<EditText>(R.id.input_crearPremioNobel).text.toString()

                if (validarFormatoFecha(nacimiento)) {
                    val newAutor = Autor(idAutor, nombre, nacimiento, nacionalidad, premioNobel)

                    db.crearAutor(newAutor).addOnSuccessListener {
                        mostrarSnackbar("El autor se ha creado exitosamente")
                    }.addOnFailureListener { e ->
                        mostrarSnackbar("Hubo un problema en la creación ")
                        Log.e("Firestore Error", e.toString())
                    }
                } else {
                    mostrarSnackbar("Error en el formato de fecha")
                }

            } catch (e: Exception) {
                Log.e("Error", "Error en la aplicación", e)
            }
        }
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.id_layout_crearAutor), texto, Snackbar.LENGTH_LONG).show()
    }

    fun validarFormatoFecha(fecha: String): Boolean {
        val regex = Regex("""^(\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$""")
        return regex.matches(fecha)
    }

    // Incluye las funciones esAñoBisiesto aquí si es necesario
}
