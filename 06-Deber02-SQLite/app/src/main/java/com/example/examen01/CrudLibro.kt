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
    var extras = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_libro)

        val idAutor = intent.extras?.getString("idAutor")




        //Crear Autor
        val btnGuardarLibro = findViewById<Button>(R.id.btn_crearLibro)
        btnGuardarLibro
            .setOnClickListener {
                try {
                    val idLibro = findViewById<EditText>(R.id.input_idLibro)
                    val titulo = findViewById<EditText>(R.id.input_creartitulo)
                    val publicacion = findViewById<EditText>(R.id.input_crearFechaPublicacion)
                    val genero = findViewById<EditText>(R.id.input_crearGenero)
                    val precio = findViewById<EditText>(R.id.input_crearPrecio)
                    val bestSeller = findViewById<EditText>(R.id.input_crearBestSeller)

                    // Limpiar errores anteriores
                    idLibro.error = null
                    titulo.error = null
                    publicacion.error = null
                    genero.error = null
                    precio.error = null
                    bestSeller.error = null

                    //mostrarSnackbar(premioNobel.text.toString())

                    if(validarFormatoFecha(publicacion.text.toString())){
                        //mostrarSnackbar("Entro")
                        val newLibro = Libro(
                            idLibro.text.toString().toInt(),
                            idAutor?.toInt(),
                            titulo.text.toString(),
                            publicacion.text.toString(),
                            genero.text.toString(),
                            precio.text.toString().toDouble(),
                            bestSeller.text.toString().toBoolean()

                        )

                        val respuesta = EBaseDeDatos.tablaLibro!!.crearLibro(newLibro)

                        if(respuesta) {
                            mostrarSnackbar("El Libro se ha creado exitosamente")

                            extras.putString("idAutor", idAutor.toString())
                            irListaLibros(DesplegarLibros::class.java, extras)

                        }else{

                            mostrarSnackbar("Hubo un problema en la creacion ")
                        }
                    }else{

                        mostrarSnackbar("Error en el formato de fecha")

                    }

                } catch (e: Exception) {

                    Log.e("Error", "Error en la aplicación", e)
                }
            }
    }

    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.id_layout_libro), //view
                texto, //texto
                Snackbar.LENGTH_LONG //tiempo
            )
            .show()
    }
    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
    fun irListaLibros(clase: Class<*>, datosExtras: Bundle? = null) {
        val intent = Intent(this, clase)
        if (datosExtras != null) {
            intent.putExtras(datosExtras)
        }
        startActivity(intent)
    }
    fun validarFormatoFecha(fecha: String): Boolean {
        // Patrón para validar el formato YYYY-MM-DD
        val regex = Regex("""^(\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$""")

        // Realizar la validación del formato
        if (!regex.matches(fecha)) {
            return false
        }

        // Desglosar la fecha en partes (año, mes, día)
        val partesFecha = fecha.split("-")
        val año = partesFecha[0].toInt()
        val mes = partesFecha[1].toInt()
        val dia = partesFecha[2].toInt()

        // Validar que el mes esté en el rango de 1 a 12
        if (mes < 1 || mes > 12) {
            return false
        }

        // Validar que el día esté en el rango adecuado para el mes
        val diasEnMes = when (mes) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (esAñoBisiesto(año)) 29 else 28
            else -> 0 // Mes no válido
        }

        return dia in 1..diasEnMes
    }

    fun esAñoBisiesto(año: Int): Boolean {
        // Regla para años bisiestos: divisible por 4 pero no por 100, o divisible por 400.
        return año % 4 == 0 && (año % 100 != 0 || año % 400 == 0)
    }

}