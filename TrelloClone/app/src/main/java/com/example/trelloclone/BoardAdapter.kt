package com.example.trelloclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R

class BoardAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    private val boardTitles = arrayOf("Figuras", "RAIO", "Concesionario")
    private val boardImages = arrayOf(
        R.drawable.figuras, // Reemplaza con tus recursos
        R.drawable.raio,
        R.drawable.concesionario
    )

    var isConcesionarioExpanded = false // Estado de expansión para 'Concesionario'

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_board, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.boardTitle.text = boardTitles[position]
        holder.boardImage.setImageResource(boardImages[position])
        val boardTitle = boardTitles[position]
        holder.boardTitle.text = boardTitle
        holder.itemView.setOnClickListener {
            onClick(boardTitle)
        }


        holder.itemView.setOnClickListener {
            onClick(boardTitles[position])
        }

        // Si el título del tablero es "Concesionario", configura el RecyclerView anidado
        if(boardTitles[position] == "Concesionario") {
            holder.nestedRecyclerView.visibility = View.VISIBLE
            holder.nestedRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.nestedRecyclerView.adapter = TaskAdapter(arrayOf("Tarea 1", "Tarea 2")) // Añade aquí los títulos reales de las tareas
        } else {
            holder.nestedRecyclerView.visibility = View.GONE
        }


        // Manejar la lógica de expansión en el clic del elemento
        holder.itemView.setOnClickListener {
            if (boardTitles[position] == "Concesionario") {
                isConcesionarioExpanded = !isConcesionarioExpanded // Cambia el estado de expansión
                notifyItemChanged(position) // Notificar para refrescar el ítem
            } else {
                onClick(boardTitles[position])
            }
        }

        // Configura el RecyclerView anidado si el ítem es 'Concesionario' y debe expandirse
        if (boardTitles[position] == "Concesionario" && isConcesionarioExpanded) {
            holder.nestedRecyclerView.visibility = View.VISIBLE
            holder.nestedRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.nestedRecyclerView.adapter = TaskAdapter(arrayOf("Tarea 1", "Tarea 2"))
        } else {
            holder.nestedRecyclerView.visibility = View.GONE
        }

    }

    override fun getItemCount() = boardTitles.size

    class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val boardImage: ImageView = itemView.findViewById(R.id.boardImage)
        val boardTitle: TextView = itemView.findViewById(R.id.boardTitle)
        val nestedRecyclerView: RecyclerView = itemView.findViewById(R.id.nestedRecyclerView)
    }
}
