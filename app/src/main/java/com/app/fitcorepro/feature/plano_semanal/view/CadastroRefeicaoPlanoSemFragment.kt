package com.app.fitcorepro.feature.plano_semanal.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.fitcorepro.R
import com.app.fitcorepro.enuns.DiaSemana
import com.app.fitcorepro.feature.plano_semanal.item.AlimentoCadastroItem
import com.app.fitcorepro.feature.plano_semanal.contract.RefeicaoContract
import com.app.fitcorepro.feature.plano_semanal.model.AlimentoPlanoSemanal
import com.app.fitcorepro.feature.plano_semanal.model.CriaRefeicaoRequest
import com.app.fitcorepro.feature.plano_semanal.presentation.RefeicaoPlanoSemPresenter
import com.google.android.material.textfield.TextInputLayout
import com.xwray.groupie.GroupieAdapter

class CadastroRefeicaoPlanoSemFragment : Fragment(), RefeicaoContract {

    private val adapter = GroupieAdapter()
    private lateinit var txtRefeicao: EditText
    private lateinit var txtAlimento: EditText
    private lateinit var txtGrama: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddAlimento: Button
    private lateinit var btnAddRefeicao: Button
    private lateinit var progress: ProgressBar
    private lateinit var RefeicaoPresenter: RefeicaoPlanoSemPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RefeicaoPresenter = RefeicaoPlanoSemPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cadastro_refeicao_plano_sem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layout_nova_refeicao_nome =
            view.findViewById<TextInputLayout>(R.id.layout_nova_refeicao_nome)
        val layout_novo_alimento_nome =
            view.findViewById<TextInputLayout>(R.id.layout_novo_alimento_nome)
        val layout_novo_grama_alimento =
            view.findViewById<TextInputLayout>(R.id.layout_novo_grama_alimento)

        val planoSemanalDiaId = arguments?.getString("planoSemanalDiaId")

        val diaSemanaAtual = arguments?.getString("diaSemanaAtual")

        if (diaSemanaAtual == null) {
            Toast.makeText(
                requireContext(),
                "Sem Dia da Semana, selecionar um dia semana",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }

        if (diaSemanaAtual == null) {
            context?.let {
                Toast.makeText(
                    it,
                    "Sem Dia da Semana, selecionar um dia semana",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (isAdded) findNavController().popBackStack()
            return
        }

        progress = view.findViewById(R.id.progress_bar)
        txtRefeicao = view.findViewById(R.id.edit_nova_refeicao_nome)
        txtAlimento = view.findViewById(R.id.edit_novo_alimento_nome)
        txtGrama = view.findViewById(R.id.add_novo_grama_alimento)
        recyclerView = view.findViewById(R.id.rv_alimentos_para_adicionar)
        btnAddRefeicao = view.findViewById(R.id.btn_add_refeicao_ao_dia)
        btnAddAlimento = view.findViewById(R.id.btn_add_alimento_a_refeicao)

        recyclerView.adapter = adapter

        btnAddAlimento.setOnClickListener {
            val alimento = txtAlimento.text.toString()
            val grama = txtGrama.text.toString()

            if (alimento.isBlank() || grama.isBlank()) {
                layout_novo_alimento_nome.error = if (alimento.isBlank()) "Obrigatório" else null
                layout_novo_grama_alimento.error = if (grama.isBlank()) "Obrigatório" else null
                return@setOnClickListener
            }

            layout_novo_grama_alimento.error = null
            layout_novo_alimento_nome.error = null

            adapter.add(AlimentoCadastroItem(alimento, grama))
            txtAlimento.text.clear()
            txtGrama.text.clear()
            txtAlimento.requestFocus()
        }

        btnAddRefeicao.setOnClickListener {
            val refeicao = txtRefeicao.text.toString()

            if (refeicao.isBlank()) {
                layout_nova_refeicao_nome.error = "Obrigatório"
                return@setOnClickListener
            }
            layout_nova_refeicao_nome.error = null

            val listaAlimentoPlanoSemanals = mutableListOf<AlimentoPlanoSemanal>()
            for (i in 0 until adapter.itemCount) {
                val item = adapter.getItem(i) as AlimentoCadastroItem
                listaAlimentoPlanoSemanals.add(
                    AlimentoPlanoSemanal(
                        "",
                        item.alimento.toString(),
                        item.gramas.toInt(),
                        ""
                    )
                )
            }

            val diaSemanaEnum = DiaSemana.valueOf(diaSemanaAtual)

            val criaRefeicaoRequest = CriaRefeicaoRequest(
                planoSemanalDiaId = planoSemanalDiaId.toString(),
                diaSemana = diaSemanaEnum.codigo,
                tipoRefeicao = refeicao,
                alimentoPlanoSemanais = listaAlimentoPlanoSemanals
            )

            RefeicaoPresenter.cadastrarRefeicao(criaRefeicaoRequest)
        }
    }

    override fun showLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress.visibility = View.GONE
    }

    override fun showMessageSuccess(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }

        if (isAdded)
            findNavController().popBackStack()
    }

    override fun onError(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }
}