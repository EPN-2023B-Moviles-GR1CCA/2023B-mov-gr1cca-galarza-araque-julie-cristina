package com.example.examen01

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class DesplegarLibros : AppCompatActivity() {
    private lateinit var listaLibrosView: ListView
    private lateinit var adaptador: ArrayAdapter<String>
    private var listaLibros: ArrayList<String> = ArrayList()
    private var listaLibrosIds: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desplegar_libros)

        val idAutor = intent.getStringExtra("idAutor") ?: return
        val nombreAutor = intent.getStringExtra("nombre")
        findViewById<TextView>(R.id.tv_NombreAutorDesL).text = nombreAutor

        listaLibrosView = findViewById(R.id.lv_list_view_libros)
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaLibros)
        listaLibrosView.adapter = adaptador

        cargarListaLibros(idAutor)

        findViewById<Button>(R.id.btn_anadir_libro).setOnClickListener {
            val intent = Intent(this, CrudLibro::class.java).apply {
                putExtra("idAutor", idAutor)
            }
            startActivity(intent)
        }

        registerForContextMenu(listaLibrosView)
    }

    private fun cargarListaLibros(idAutor: String) {
        BDD.bddAplicacion?.obtenerLibrosPorAutor(idAutor) { libros ->
            listaLibros.clear()
            listaLibrosIds.clear()
            libros.forEach { libro ->
                listaLibros.add(libro.titulo ?: "Sin título")
                listaLibrosIds.add(libro.idLibro.toString())
            }
            adaptador.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        val idAutor = intent.getStringExtra("idAutor")
        idAutor?.let {
            cargarListaLibros(it) // Recargar la lista de libros cuando la actividad se reanuda
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_libro, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            R.id.mi_editar -> {
                editarLibro(info.position)
                return true
            }
            R.id.mi_eliminar -> {
                eliminarLibro(info.position)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun editarLibro(posicion: Int) {
        val libroId = listaLibrosIds[posicion]
        val idAutor = intent.getStringExtra("idAutor")
        val intent = Intent(this, ActualizarLibro::class.java).apply {
            putExtra("idLibro", libroId)
            putExtra("idAutore", idAutor)
        }
        startActivity(intent)
    }

    private fun eliminarLibro(posicion: Int) {
        val libroId = listaLibrosIds[posicion]
        BDD.bddAplicacion?.eliminarLibroPorId(intent.getStringExtra("idAutor") ?: return, libroId)
            ?.addOnSuccessListener {
                Snackbar.make(findViewById(R.id.id_layout_desplegar_libros), "Libro eliminado exitosamente", Snackbar.LENGTH_LONG).show()
                val idAutor = intent.getStringExtra("idAutor")
                idAutor?.let { cargarListaLibros(it) }
            }
            ?.addOnFailureListener { e ->
                Snackbar.make(findViewById(R.id.id_layout_desplegar_libros), "Error al eliminar libro: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
    }


    // ... Resto del código de la clase DesplegarLibros (métodos onCreateContextMenu, onContextItemSelected, etc.) ...
}
