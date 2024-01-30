package com.example.examen01

import Autor
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var listaAutores: ListView
    private lateinit var btnAnadirAutor: Button
    private lateinit var adaptador: ArrayAdapter<Autor>
    var listaAutoresDB = EBaseDeDatos.tablaAutor?.consultarAutores()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //la base de datos
        EBaseDeDatos.tablaAutor = RBaseDeDatos(this)
        EBaseDeDatos.tablaLibro = RBaseDeDatos(this)


        listaAutores = findViewById(R.id.lv_autores)
        btnAnadirAutor = findViewById(R.id.btn_anadirAutor)

        cargarAutores()

        btnAnadirAutor.setOnClickListener {
            // Lanzar la actividad para agregar autor
            val intent = Intent(this, CrudAutor2::class.java)
            startActivity(intent)
        }

        registerForContextMenu(listaAutores)
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
        inflater.inflate(R.menu.menu_autor, menu)
        //obtener el id del ArrayListSeleccionado
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val idAutor = listaAutoresDB!!.get(posicionItemSeleccionado).idAutor
        val nombre = listaAutoresDB!!.get(posicionItemSeleccionado).nombre


        return when (item.itemId) {
            R.id.mi_editar -> {
                mostrarSnackbar("${posicionItemSeleccionado}")
                val extras = Bundle()
                extras.putString("idAutor", idAutor.toString())
                irEdicionAutor(ActualizarAutor::class.java, extras)
                //irActividad(ActualizarAutor::class.java)
                true
            }
            R.id.mi_eliminar -> {
                abrirDialogo()


                true
            }
            R.id.mi_ver_libros -> {
                // Implementa la lógica para ver libros aquí
                // Puedes abrir otra actividad o realizar alguna acción específica
                val extras = Bundle()
                extras.putString("idAutor", idAutor.toString())
                extras.putString("nombre", nombre.toString())

                irEdicionAutor(DesplegarLibros::class.java, extras)
                true
            }
            else -> super.onContextItemSelected(item)
        }



    }


    override fun onResume() {
        super.onResume()
        cargarAutores() // Actualizar la lista al volver a la actividad principal
    }


    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
    private fun cargarAutores() {
        // Obtener la lista de autores desde la base de datos
        listaAutoresDB = EBaseDeDatos.tablaAutor?.consultarAutores()
        // Configurar el adaptador
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaAutoresDB.orEmpty())

        // Asignar el adaptador al ListView
        listaAutores.adapter = adaptador
        adaptador.notifyDataSetChanged()
    }
    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.lv_autores), //view
                texto, //texto
                Snackbar.LENGTH_LONG //tiwmpo
            )
            .show()
    }
    fun irEdicionAutor(clase: Class<*>, datosExtras: Bundle? = null) {
        val intent = Intent(this, clase)
        if (datosExtras != null) {
            intent.putExtras(datosExtras)
        }
        startActivity(intent)
    }

    fun abrirDialogo(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                val respuesta = EBaseDeDatos.tablaAutor!!.eliminarAutor(listaAutoresDB!!.get(posicionItemSeleccionado).idAutor!!.toInt())
                if (respuesta){
                    mostrarSnackbar("Autor eliminado exitosamente")
                    cargarAutores()
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






}
