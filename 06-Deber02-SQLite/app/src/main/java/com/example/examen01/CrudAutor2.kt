package com.example.examen01

import Autor
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class CrudAutor2 : AppCompatActivity() {
    companion object{
        var idAutorSeleccionado = 0
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_autor2) // Asegúrate de cambiar "tu_layout" al nombre correcto
        //regresar a la list view
        val btnVolverMain = findViewById<Button>(R.id.btn_volverMain2)
        btnVolverMain
            .setOnClickListener{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        //Crear Autor
        val btnGuardarAutor = findViewById<Button>(R.id.btn_crearAutor2)
        btnGuardarAutor
            .setOnClickListener {
                try {
                    val idAutor = findViewById<EditText>(R.id.input_crearIdAutor)
                    val nombre = findViewById<EditText>(R.id.input_crearNombreAutor)
                    val nacimiento = findViewById<EditText>(R.id.input_crearNacimiento)
                    val nacionalidad = findViewById<EditText>(R.id.input_crearNacionalidadAutor)
                    val premioNobel = findViewById<EditText>(R.id.input_crearPremioNobel)

                    // Limpiar errores anteriores
                    idAutor.error = null
                    nombre.error = null
                    nacimiento.error = null
                    nacionalidad.error = null
                    premioNobel.error = null

                    //mostrarSnackbar(premioNobel.text.toString())

                    if(validarFormatoFecha(nacimiento.text.toString())){
                        //mostrarSnackbar("Entro")
                        val newAutor = Autor(
                            idAutor.text.toString().toInt(),
                            nombre.text.toString(),
                            nacimiento.text.toString(),
                            nacionalidad.text.toString(),
                            premioNobel.text.toString()
                        )

                        val respuesta = EBaseDeDatos
                            .tablaAutor!!.crearAutor(newAutor)

                        if(respuesta) {
                            mostrarSnackbar("El autor se ha creado exitosamente")
                            irActividad(MainActivity::class.java)
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
                findViewById(R.id.id_layout_crearAutor), //view
                texto, //texto
                Snackbar.LENGTH_LONG //tiempo
            )
            .show()
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
    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }



}

