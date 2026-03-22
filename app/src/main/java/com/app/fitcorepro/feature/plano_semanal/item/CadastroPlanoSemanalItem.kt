package com.app.fitcorepro.feature.plano_semanal.item

import android.view.View
import android.widget.TextView
import com.app.fitcorepro.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class CadastroPlanoSemanalItem(
    private val refeicao: String,
    private val alimentos: String
) : Item<CadastroPlanoSemanalItem.CadastroPlanoSemanalViewHolder>() {

    class CadastroPlanoSemanalViewHolder(itemView: View) : GroupieViewHolder(itemView)

    override fun createViewHolder(itemView: View) = CadastroPlanoSemanalViewHolder(itemView)

    override fun bind(viewHolder: CadastroPlanoSemanalViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.nome_refeicao_added).text = refeicao
        viewHolder.itemView.findViewById<TextView>(R.id.nome_alimento_added).text = alimentos
    }

    override fun getLayout() = R.layout.item_refeicao_cadastro_plano_semanal
}