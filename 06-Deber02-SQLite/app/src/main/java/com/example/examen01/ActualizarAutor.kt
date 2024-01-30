package com.example.examen01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class ActualizarAutor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_autor)


        val idAutor = intent.extras?.getString("idAutor")

        val nombre = findViewById<EditText>(R.id.input_actuNombreAutor)
        val fechaNacimiento = findViewById<EditText>(R.id.input_actuFechaNacimiento)
        val nacionalidad = findViewById<EditText>(R.id.input_actuNacionalidad)
        val premioNobel = findViewById<EditText>(R.id.input_actualizarPremioNovel)
        val autorEn = EBaseDeDatos.tablaAutor!!.consultarAutorPorID(idAutor!!.toInt())


        if(autorEn.idAutor == 0){
            mostrarSnackbar("Usuario no encontrado")
        }
        nombre.setText(autorEn.nombre)
        fechaNacimiento.setText(autorEn.fechaNacimiento)
        nacionalidad.setText(autorEn.nacionalidad)
        premioNobel.setText(autorEn.premioNobel)
        mostrarSnackbar("Usu. encontrado")

        val botonActualizarAutor = findViewById<Button>(R.id.btn_actualizarAutor)
        botonActualizarAutor
            .setOnClickListener{
                val respuesta = EBaseDeDatos.tablaAutor!!.actualizarAutor(
                    idAutor.toInt(),
                    nombre.text.toString(),
                    fechaNacimiento.text.toString(),
                    nacionalidad.text.toString(),
                    premioNobel.text.toString()
                )
                if (respuesta){
                    mostrarSnackbar("Autor actualizado con éxito")
                    irActividad(MainActivity::class.java)
                }else{
                    mostrarSnackbar("Ocurrio un error al momento de actualizar")
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