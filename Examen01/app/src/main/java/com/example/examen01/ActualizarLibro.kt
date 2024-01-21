package com.example.examen01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.examen01.R
import com.google.android.material.snackbar.Snackbar


class ActualizarLibro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_libro)

        val idLibro = intent.extras?.getString("idLibro")
        val idAutor = intent.extras?.getString("idAutor")

        mostrarSnackbar(idAutor.toString())

        val idLibroAu = idLibro?.toInt()
        val idAutorAu = idAutor?.toInt()

        val titulo = findViewById<EditText>(R.id.input_titulo)
        val fecha = findViewById<EditText>(R.id.input_fechaPublicacion)
        val genero = findViewById<EditText>(R.id.input_genero)
        val precio = findViewById<EditText>(R.id.input_precio)
        val bestSeller = findViewById<EditText>(R.id.input_bestSeller)

        if (idLibroAu != null && idAutorAu != null) {
            val libroEn = EBaseDeDatos.tablaLibro?.consultarLibroPorID(idLibroAu, idAutorAu)

            if (libroEn?.idLibro == 0) {
                mostrarSnackbar("Libro no encontrado")
            } else {
                titulo.setText(libroEn?.titulo)
                fecha.setText(libroEn?.fechaPublicacion)
                genero.setText(libroEn?.genero)
                precio.setText(libroEn?.precio.toString())
                bestSeller.setText(libroEn?.bestSeller.toString())
            }

            val botonActualizarLibro = findViewById<Button>(R.id.btn_actualizar_libro)
            botonActualizarLibro
                .setOnClickListener{
                    //val resultBoolean = if (bestSeller.text.toString().toBoolean()) 1 else 0
                    val respuesta = EBaseDeDatos.tablaLibro!!.actualizarLibro(
                        idLibroAu,
                        idAutorAu,
                        titulo.text.toString(),
                        fecha.text.toString(),
                        genero.text.toString(),
                        precio.text.toString().toDouble(),
                        bestSeller.text.toString().toBoolean()
                    )

                    if (respuesta) {
                        mostrarSnackbar("Libro actualizado con éxito")
                        val extras = Bundle()
                        extras.putString("idAutor", idAutor.toString())
                        irEdicionLibro(DesplegarLibros::class.java, extras)
                    } else {
                        mostrarSnackbar("Ocurrió un error al momento de actualizar")
                    }



                }

        }



    }
    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.id_layout_actualizar_libro), //view
                texto, //texto
                Snackbar.LENGTH_LONG //tiwmpo
            )
            .show()
    }
    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
    fun irEdicionLibro(clase: Class<*>, datosExtras: Bundle? = null) {
        val intent = Intent(this, clase)
        if (datosExtras != null) {
            intent.putExtras(datosExtras)
        }
        startActivity(intent)
    }
}