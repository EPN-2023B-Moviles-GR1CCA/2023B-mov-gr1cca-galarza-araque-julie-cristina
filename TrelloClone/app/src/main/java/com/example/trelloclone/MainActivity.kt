package com.example.trelloclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var boardsRecyclerView: RecyclerView
    private lateinit var boardAdapter: BoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boardsRecyclerView = findViewById(R.id.boardsRecyclerView)
        boardsRecyclerView.layoutManager = LinearLayoutManager(this)

        boardAdapter = BoardAdapter { boardTitle ->
            if (boardTitle == "Concesionario") {
                // Aquí manejas la lógica de clic, como cambiar la visibilidad del RecyclerView anidado
                // o iniciar una nueva actividad/fragmento.
            }
        }

        //boardAdapter = BoardAdapter()
        boardsRecyclerView.adapter = boardAdapter
    }
}
