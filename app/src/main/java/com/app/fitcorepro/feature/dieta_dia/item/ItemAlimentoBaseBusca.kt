package com.app.fitcorepro.feature.dieta_dia.item

import android.view.View
import android.widget.TextView
import com.app.fitcorepro.R
import com.app.fitcorepro.model.AlimentoBase
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ItemAlimentoBaseBusca(
    private val alimentoBase: AlimentoBase,
    private val onClickListener: (AlimentoBase) -> Unit // O construtor agora espera uma função
) : Item<ItemAlimentoBaseBusca.AlimentoBaseBuscaViewHolder>() {

    override fun createViewHolder(itemView: View) = AlimentoBaseBuscaViewHolder(itemView)

    override fun getLayout() = R.layout.item_alimento_base_busca

    class AlimentoBaseBuscaViewHolder(itemView: View) : GroupieViewHolder(itemView)

    override fun bind(viewHolder: AlimentoBaseBuscaViewHolder, position: Int) {
        val view = viewHolder.itemView
        view.findViewById<TextView>(R.id.label_alimento).text = alimentoBase.nome
        view.findViewById<TextView>(R.id.label_gramas).text = "${alimentoBase.gramas}g"
        view.findViewById<TextView>(R.id.label_Carb).text = "Carb. ${alimentoBase.carboidratos}g"
        view.findViewById<TextView>(R.id.label_Prot).text = "Prot. ${alimentoBase.proteinas}g"
        view.findViewById<TextView>(R.id.label_gor).text = "Gord. ${alimentoBase.gorduras}g"
        view.findViewById<TextView>(R.id.label_fib).text = "Fib. ${alimentoBase.fibras}g"

        // Adiciona o listener de clique à view do item
        view.setOnClickListener {
            onClickListener(alimentoBase) // Chama a função que foi passada pelo construtor
        }
    }
}