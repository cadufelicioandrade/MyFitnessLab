package com.app.fitcorepro.feature.plano_semanal.item

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.plano_semanal.model.AlimentoPlanoSemanal
import com.app.fitcorepro.feature.plano_semanal.model.RefeicaoPlanoSemanal
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class RefeicaoPlanoSemanalItem(
    private val refeicaoPlanoSemanal: RefeicaoPlanoSemanal,
    val onRemoveRefeicao: (refeicaoPlanoSemanal: RefeicaoPlanoSemanal) -> Unit,
    val onAddAlimento: (refeicaoPlanoSemanal: RefeicaoPlanoSemanal) -> Unit,
    val onEditAlimento: (alimentoPlanoSemanal: AlimentoPlanoSemanal) -> Unit,
    val onRemoveAlimento: (alimentoPlanoSemanal: AlimentoPlanoSemanal) -> Unit
) : Item<RefeicaoPlanoSemanalItem.RefeicaoViewHolder>() {

    private var isExpanded = true
    private lateinit var deleteRefeicao: ImageButton
    private lateinit var addAlimento: ImageButton

    class RefeicaoViewHolder(itemView: View) : GroupieViewHolder(itemView)

    override fun createViewHolder(itemView: View) = RefeicaoViewHolder(itemView)

    @SuppressLint("NotifyDataSetChanged")
    override fun bind(viewHolder: RefeicaoViewHolder, position: Int) {
        refeicaoPlanoSemanal.let {
            viewHolder.itemView.findViewById<TextView>(R.id.title_card).text = it.tipo
            val recyclerView = viewHolder.itemView.findViewById<RecyclerView>(R.id.rc_refeicao_plano_semanal)
            val adapter = GroupieAdapter()
            recyclerView.layoutManager = LinearLayoutManager(viewHolder.itemView.context)
            recyclerView.adapter = adapter
            val alimentoItems =
                it.alimentoPlanoSemanais.map { alimento ->
                    AlimentoItem(
                        alimento,
                        { onEditAlimento(alimento) },
                        { onRemoveAlimento(alimento) }
                    )
                }
            adapter.addAll(alimentoItems)
            adapter.notifyDataSetChanged()
            deleteRefeicao = viewHolder.itemView.findViewById(R.id.btn_remove_refeicao)
            addAlimento = viewHolder.itemView.findViewById(R.id.fab_add_alimento)
            deleteRefeicao.setOnClickListener {
                onRemoveRefeicao(refeicaoPlanoSemanal)
            }
            addAlimento.setOnClickListener { onAddAlimento(refeicaoPlanoSemanal) }
            updateVisibility(viewHolder.itemView)

            viewHolder.itemView.findViewById<ImageButton>(R.id.btn_toggle_visibility_refeicao)
                .setOnClickListener {
                    toggleVisibility(viewHolder.itemView)
                }

            viewHolder.itemView.findViewById<TextView>(R.id.title_card).setOnClickListener {
                toggleVisibility(viewHolder.itemView)
            }
        }
    }

    override fun getLayout() = R.layout.card_plano_semanal

    private fun toggleVisibility(itemView: View) {
        isExpanded = !isExpanded
        updateVisibility(itemView)
    }

    private fun updateVisibility(itemView: View) {
        itemView.findViewById<RecyclerView>(R.id.rc_refeicao_plano_semanal).isVisible = isExpanded
        itemView.findViewById<FloatingActionButton>(R.id.fab_add_alimento).isVisible = isExpanded
        itemView.findViewById<ImageButton>(R.id.btn_remove_refeicao).isVisible = isExpanded

        val toggleButton = itemView.findViewById<ImageButton>(R.id.btn_toggle_visibility_refeicao)
        val iconRes = if (isExpanded) R.drawable.outline_expand_circle_down_24
        else R.drawable.outline_expand_circle_up_24
        toggleButton.setImageResource(iconRes)
    }

}