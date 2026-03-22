package com.app.fitcorepro.feature.dieta_dia.item

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.dieta_dia.model.AlimentoDietaDia
import com.app.fitcorepro.feature.dieta_dia.model.RefeicaoDietaDia
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class RefeicaoDietaDiaItem(
    private val refeicaoDietaDia: RefeicaoDietaDia,
    val onRemoveRefeicao: (refeicaoDietaDia: RefeicaoDietaDia) -> Unit,
    val onAddAlimento: (refeicaoDietaDia: RefeicaoDietaDia) -> Unit,
    val onEditAlimento: (alimentoDietaDia: AlimentoDietaDia) -> Unit,
    val onRemoveAlimento: (alimentoDietaDia: AlimentoDietaDia) -> Unit
) : Item<RefeicaoDietaDiaItem.RefeicaoViewHolder>() {

    private var isExpanded = true
    private lateinit var recyclerView: RecyclerView
    private val adapter = GroupieAdapter()
    private lateinit var txtCarboidrato: TextView
    private lateinit var txtProteina: TextView
    private lateinit var txtGordura: TextView
    private lateinit var txtFibra: TextView
    private lateinit var txtKcal: TextView
    private lateinit var deleteRefeicao: ImageButton
    private lateinit var addAlimento: ImageButton
    private lateinit var toggleButton: ImageButton
    private lateinit var headerMacros: LinearLayout

    class RefeicaoViewHolder(itemView: View) : GroupieViewHolder(itemView)

    override fun createViewHolder(itemView: View) = RefeicaoViewHolder(itemView)

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: RefeicaoViewHolder, position: Int) {

        getElementsLayout(viewHolder)

        refeicaoDietaDia.let {
            viewHolder.itemView.findViewById<TextView>(R.id.title_card).text = it.titulo

            val carb = it.alimentos.sumOf { alimento -> alimento.carboidratos }
            val prot = it.alimentos.sumOf { alimento -> alimento.proteinas }
            val gor = it.alimentos.sumOf { alimento -> alimento.gorduras }
            val fib = it.alimentos.sumOf { alimento -> alimento.fibras }
            val kcal = it.alimentos.sumOf { alimento -> alimento.calorias }

            txtCarboidrato.text = "Carb: ${carb}"
            txtProteina.text = "Prot: ${prot}"
            txtGordura.text = "Gord: ${gor}"
            txtFibra.text = "Fib: ${fib}"
            txtKcal.text = "Kcal: ${kcal}"
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(viewHolder.itemView.context)

            deleteRefeicao.setOnClickListener { onRemoveRefeicao(refeicaoDietaDia) }
            addAlimento.setOnClickListener { onAddAlimento(refeicaoDietaDia) }

            toggleButton.setOnClickListener {
                isExpanded = !isExpanded
                recyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
                headerMacros.visibility = if (isExpanded) View.VISIBLE else View.GONE
                deleteRefeicao.visibility = if (isExpanded) View.VISIBLE else View.GONE
                addAlimento.visibility = if (isExpanded) View.VISIBLE else View.GONE
            }

            val alimentoItems =
                it.alimentos.map { alimento ->
                    AlimentoDietaDiaItem(
                        alimento,
                        { onEditAlimento(alimento) },
                        { onRemoveAlimento(alimento) }
                    )
                }

            adapter.addAll(alimentoItems)
        }
    }

    override fun getLayout() = R.layout.card_dieta_dia

    private fun getElementsLayout(viewHolder: RefeicaoViewHolder) {
        txtCarboidrato = viewHolder.itemView.findViewById(R.id.carboidrato_card)
        txtProteina = viewHolder.itemView.findViewById(R.id.proteina_card)
        txtGordura = viewHolder.itemView.findViewById(R.id.gordura_card)
        txtFibra = viewHolder.itemView.findViewById(R.id.fibra_card)
        txtKcal = viewHolder.itemView.findViewById(R.id.Kcal_card)
        recyclerView = viewHolder.itemView.findViewById(R.id.rc_dieta_dia)
        deleteRefeicao = viewHolder.itemView.findViewById(R.id.btn_remove_refeicao)
        addAlimento = viewHolder.itemView.findViewById(R.id.fab_add_alimento)
        toggleButton = viewHolder.itemView.findViewById(R.id.btn_toggle_visibility_refeicao)
        headerMacros = viewHolder.itemView.findViewById(R.id.header_macros_layout)

    }

}