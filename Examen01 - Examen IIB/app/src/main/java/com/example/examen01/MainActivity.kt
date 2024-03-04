package com.example.examen01


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var listaAutores: ListView
    private lateinit var btnAnadirAutor: Button
    private lateinit var adaptador: ArrayAdapter<String>
    private var listaIdsAutores = mutableListOf<String>() // Lista para guardar los IDs de Firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listaAutores = findViewById(R.id.lv_autores)
        btnAnadirAutor = findViewById(R.id.btn_anadirAutor)

        btnAnadirAutor.setOnClickListener {
            startActivity(Intent(this, CrudAutor2::class.java))
        }

        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listaAutores.adapter = adaptador
        registerForContextMenu(listaAutores)

        cargarAutoresDesdeFirestore()
    }

    private fun cargarAutoresDesdeFirestore() {
        db.collection("autores")
            .get()
            .addOnSuccessListener { result ->
                val nombresAutores = mutableListOf<String>()
                listaIdsAutores.clear()
                for (documento in result) {
                    val autor = documento.getString("nombre") ?: "Sin Nombre"
                    nombresAutores.add(autor)
                    listaIdsAutores.add(documento.id)
                }
                adaptador.clear()
                adaptador.addAll(nombresAutores)
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Snackbar.make(listaAutores, "Error al cargar datos: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_autor, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.mi_editar -> {
                val idAutor = listaIdsAutores[info.position]
                val intent = Intent(this, ActualizarAutor::class.java).apply {
                    putExtra("idAutor", idAutor)
                }
                startActivity(intent)
                true
            }
            R.id.mi_eliminar -> {
                val idAutor = listaIdsAutores[info.position]
                mostrarDialogoConfirmacion(idAutor)
                true
            }
            R.id.mi_ver_libros -> {
                val idAutor = listaIdsAutores[info.position]
                val intent = Intent(this, DesplegarLibros::class.java).apply {
                    putExtra("idAutor", idAutor)                 }
                startActivity(intent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun mostrarDialogoConfirmacion(idAutor: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Eliminar Autor")
            setMessage("¿Estás seguro de querer eliminar este autor?")
            setPositiveButton("Sí") { dialog, which ->
                eliminarAutor(idAutor)
            }
            setNegativeButton("No", null)
        }.show()
    }

    private fun eliminarAutor(idAutor: String) {
        db.collection("autores").document(idAutor)
            .delete()
            .addOnSuccessListener {
                Snackbar.make(listaAutores, "Autor eliminado correctamente", Snackbar.LENGTH_LONG).show()
                cargarAutoresDesdeFirestore()
            }
            .addOnFailureListener { e ->
                Snackbar.make(listaAutores, "Error al eliminar autor: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
    }
}