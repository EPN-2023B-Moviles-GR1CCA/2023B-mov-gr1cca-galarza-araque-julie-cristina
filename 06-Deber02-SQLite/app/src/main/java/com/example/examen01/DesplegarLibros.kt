package com.example.examen01

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.google.android.material.snackbar.Snackbar

class DesplegarLibros : AppCompatActivity() {
    private lateinit var listaLibro: ListView
    private lateinit var adaptador: ArrayAdapter<Libro>
    private lateinit var btnAnadirLibro: Button
    var listaLibroDB = EBaseDeDatos.tablaLibro!!.consultarLibros()

    companion object{
        var idLibroSeleccionado = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desplegar_libros)


        val idAutor = intent.extras?.getString("idAutor")
        val nombre = intent.extras?.getString("nombre")
        val nombreAutor = findViewById<TextView>(R.id.tv_NombreAutorDesL)

        nombreAutor.setText(nombre)

        listaLibro = findViewById(R.id.lv_list_view_libros)





        cargarLibros(idAutor!!.toInt())

        val btnAnadirLibro = findViewById<Button>(R.id.btn_anadir_libro)
            .setOnClickListener {
                val extras = Bundle()
                extras.putString("idAutor", idAutor.toString())
                irEdicionLibro(CrudLibro::class.java, extras)

        }

        registerForContextMenu(listaLibro)



    }
    var posicionItemSeleccionado = 0

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //llenamos las opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_libro, menu)
        //obtener el id del ArrayListSeleccionado
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val idLibro = listaLibroDB!!.get(posicionItemSeleccionado).idLibro
        val idAutor = listaLibroDB!!.get(posicionItemSeleccionado).idAutor


        return when (item.itemId) {
            R.id.mi_editar -> {
                mostrarSnackbar("${posicionItemSeleccionado}")
                val extras = Bundle()
                extras.putString("idLibro", idLibro.toString())
                extras.putString("idAutor",idAutor.toString())
                irEdicionLibro(ActualizarLibro::class.java, extras)
                true
            }
            R.id.mi_eliminar -> {
                abrirDialogo()
                true
            }
            else -> super.onContextItemSelected(item)
        }



    }


    override fun onResume() {
        val idAutor = intent.extras?.getString("idAutor")
        super.onResume()
        cargarLibros(idAutor!!.toInt()) // Actualizar la lista al volver a la actividad principal
    }

    private fun cargarLibros(idAutor: Int) {
        // Verificar que el ID del autor no sea nulo
        if (idAutor != 0) {
            // Obtener la lista de libros específicos del autor
            listaLibroDB = EBaseDeDatos.tablaLibro!!.consultarLibrosPorAutor(idAutor)

            // Configurar el adaptador
            adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaLibroDB)

            // Asignar el adaptador al ListView
            listaLibro.adapter = adaptador
            adaptador.notifyDataSetChanged()
        } else {
            mostrarSnackbar("Error al obtener información del autor")
        }
    }



    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.id_layout_desplegar_libros), //view
                texto, //texto
                Snackbar.LENGTH_LONG //tiwmpo
            )
            .show()
    }


    fun abrirDialogo(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                val respuesta = EBaseDeDatos.tablaLibro!!.eliminarLibro(listaLibroDB!!.get(posicionItemSeleccionado).idLibro!!.toInt())
                if (respuesta){
                    mostrarSnackbar("Libro eliminado exitosamente")
                    cargarLibros(listaLibroDB!!.get(posicionItemSeleccionado).idAutor!!.toInt())
                }else{
                    mostrarSnackbar("Ocurrio un error al momento de eliminar")
                }
            }
        )
        builder.setNegativeButton(
            "Cancelar",
            null
        )

        val dialogo = builder.create()
        dialogo.show()
    }
    fun irEdicionLibro(clase: Class<*>, datosExtras: Bundle? = null) {
        val intent = Intent(this, clase)
        if (datosExtras != null) {
            intent.putExtras(datosExtras)
        }
        startActivity(intent)
    }
}