package com.app.fitcorepro.feature.treinamento.item

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.treinamento.model.Treino
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class TreinoItem(private val treino: Treino?) : Item<TreinoItem.TreinoViewHolder>() {

    private var isExpanded = true

    constructor() : this(null)

    class TreinoViewHolder(itemView: View) : GroupieViewHolder(itemView)

    override fun createViewHolder(itemView: View) = TreinoViewHolder(itemView)

    override fun bind(viewHolder: TreinoViewHolder, position: Int) {
        treino?.let {
            viewHolder.itemView.findViewById<TextView>(R.id.txt_dia_semana).text = it.diaSemana
            val recyclerView = viewHolder.itemView.findViewById<RecyclerView>(R.id.rc_card_treino)
            val adapter = GroupieAdapter()
            recyclerView.layoutManager = LinearLayoutManager(viewHolder.itemView.context)
            recyclerView.adapter = adapter
            val exercicioItems = it.exercicios.map { exercicio -> ExercicioItem(exercicio) }
            adapter.addAll(exercicioItems)

            updateVisibility(viewHolder.itemView)

            viewHolder.itemView.findViewById<ImageButton>(R.id.btn_toggle_visibility_refeicao)
                .setOnClickListener {
                    toggleVisibility(viewHolder.itemView)
                }

            viewHolder.itemView.findViewById<TextView>(R.id.txt_dia_semana).setOnClickListener {
                toggleVisibility(viewHolder.itemView)
            }
        }
    }

    override fun getLayout() = R.layout.card_treino

    private fun toggleVisibility(itemView: View) {
        isExpanded = !isExpanded
        updateVisibility(itemView)
    }

    private fun updateVisibility(itemView: View) {
        itemView.findViewById<RecyclerView>(R.id.rc_card_treino).isVisible = isExpanded
        itemView.findViewById<FloatingActionButton>(R.id.fab_add_treino).isVisible = isExpanded

        val toggleButton = itemView.findViewById<ImageButton>(R.id.btn_toggle_visibility_refeicao)
        val iconRes = if (isExpanded) R.drawable.outline_expand_circle_down_24
                        else R.drawable.outline_expand_circle_up_24
        toggleButton.setImageResource(iconRes)
    }

}