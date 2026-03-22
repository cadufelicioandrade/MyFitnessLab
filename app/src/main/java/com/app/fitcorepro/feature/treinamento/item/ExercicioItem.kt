package com.app.fitcorepro.feature.treinamento.item

import android.view.View
import android.widget.TextView
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.treinamento.model.Exercicio
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ExercicioItem(private val exercicio: Exercicio?) : Item<ExercicioItem.ExercicioViewHolder>() {

    class ExercicioViewHolder(itemView: View) : GroupieViewHolder(itemView)

    override fun createViewHolder(itemView: View) = ExercicioViewHolder(itemView)

    override fun bind(viewHolder: ExercicioViewHolder, position: Int) {
        exercicio?.let{
            viewHolder.itemView.findViewById<TextView>(R.id.txt_exercicio).text = it.tipo
            viewHolder.itemView.findViewById<TextView>(R.id.txt_series).text = it.series.toString()
        }

    }

    override fun getLayout() = R.layout.item_treino
}