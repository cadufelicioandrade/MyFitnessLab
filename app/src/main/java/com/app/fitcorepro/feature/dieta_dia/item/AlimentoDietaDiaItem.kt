package com.app.fitcorepro.feature.dieta_dia.item

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.dieta_dia.model.AlimentoDietaDia
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class AlimentoDietaDiaItem(
    private val alimentoDietaDia: AlimentoDietaDia,
    val onEditClicked: (alimentoDietaDia: AlimentoDietaDia) -> Unit,
    val onRemoveClicked: (alimentoDietaDia: AlimentoDietaDia) -> Unit
) : Item<AlimentoDietaDiaItem.AlimentoViewHolder>() {

    private lateinit var editAlimento: ImageButton
    private lateinit var removeAlimento: ImageButton

    override fun createViewHolder(itemView: View) = AlimentoViewHolder(itemView)

    class AlimentoViewHolder(itemView: View) : GroupieViewHolder(itemView)

    override fun bind(viewHolder: AlimentoViewHolder, position: Int) {

        alimentoDietaDia.let {

            viewHolder.itemView.findViewById<TextView>(R.id.nome_alimento).text = it.nome
            viewHolder.itemView.findViewById<TextView>(R.id.gramas_alimento).text = "${it.quantidadeGrama}g"

            editAlimento = viewHolder.itemView.findViewById(R.id.btn_edit_item_refeicao)
            removeAlimento = viewHolder.itemView.findViewById(R.id.btn_remove_item_refeicao)

            editAlimento.setOnClickListener {
                onEditClicked(alimentoDietaDia)
            }

            removeAlimento.setOnClickListener {
                onRemoveClicked(alimentoDietaDia)
            }
        }
    }

    override fun getLayout() = R.layout.item_alimento_dieta_dia

}