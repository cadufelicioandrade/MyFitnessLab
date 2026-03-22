package com.app.fitcorepro.feature.plano_semanal.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.plano_semanal.item.AlimentoCadastroItem
import com.app.fitcorepro.feature.plano_semanal.contract.AlimentoContract
import com.app.fitcorepro.feature.plano_semanal.model.AlimentoPlanoSemanal
import com.app.fitcorepro.feature.plano_semanal.presentation.AlimentoPlanoSemPresenter
import com.google.android.material.textfield.TextInputLayout
import com.xwray.groupie.GroupieAdapter

class CadastroListaDeAlimentoPlanoSemFragment : Fragment(), AlimentoContract {

    private val adapter: GroupieAdapter = GroupieAdapter()
    private lateinit var txtTituloRefeicao: TextView
    private lateinit var txtNomeAlimento: EditText
    private lateinit var txtGramaAlimento: EditText
    private lateinit var btnAdicionarAlimento: Button
    private lateinit var btnAcicionarRefeicao: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var presenter: AlimentoPlanoSemPresenter
    private lateinit var progress: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AlimentoPlanoSemPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_cadastro_lista_de_alimento_plano_sem,
            container,
            false
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layout_novo_alimento_nome =
            view.findViewById<TextInputLayout>(R.id.layout_novo_alimento_nome)
        val layout_novo_grama_alimento =
            view.findViewById<TextInputLayout>(R.id.layout_novo_grama_alimento)

        val refeitcaoId = arguments?.getString("refeicaoId")!!
        val tipoRefeicao = arguments?.getString("tipoRefeicao")

        txtTituloRefeicao = view.findViewById(R.id.titulo_refeicao_cadastro)
        txtNomeAlimento = view.findViewById(R.id.edit_novo_alimento_nome)
        txtGramaAlimento = view.findViewById(R.id.add_novo_grama_alimento)
        btnAdicionarAlimento = view.findViewById(R.id.btn_add_alimento_a_refeicao)
        btnAcicionarRefeicao = view.findViewById(R.id.btn_add_refeicao_ao_dia)
        progress = view.findViewById(R.id.progress_bar_dieta)
        recyclerView = view.findViewById(R.id.rv_alimentos_para_adicionar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        txtTituloRefeicao.text = tipoRefeicao

        btnAdicionarAlimento.setOnClickListener {
            val nomeAlimento = txtNomeAlimento.text.toString()
            val grama = txtGramaAlimento.text.toString()

            if (nomeAlimento.isEmpty() || grama.isEmpty()) {

                if (nomeAlimento.isEmpty()) layout_novo_alimento_nome.error = "Obrigatório"
                if (grama.isEmpty()) layout_novo_grama_alimento.error = "Obrigatório"

                return@setOnClickListener
            }
            layout_novo_alimento_nome.error = null
            layout_novo_grama_alimento.error = null

            adapter.add(AlimentoCadastroItem(nomeAlimento, grama))
            txtNomeAlimento.text.clear()
            txtGramaAlimento.text.clear()
            txtNomeAlimento.requestFocus()
        }

        btnAcicionarRefeicao.setOnClickListener {

            if (adapter.itemCount == 0) {
                Toast.makeText(
                    requireContext(),
                    "Inclua pelo menos um alimento para salvar a refeição.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val listAlimentoPlanoSemanals = mutableListOf<AlimentoPlanoSemanal>()

            for (i in 0 until adapter.itemCount) {
                val item = adapter.getItem(i) as AlimentoCadastroItem
                listAlimentoPlanoSemanals.add(
                    AlimentoPlanoSemanal(
                        "",
                        item.alimento.toString(),
                        item.gramas.toInt(),
                        refeitcaoId
                    )
                )
            }

            presenter.addListAlimentos(listAlimentoPlanoSemanals)
            findNavController().navigate(R.id.action_nav_new_aliment_to_nav_diet_week)
        }
    }

    override fun showLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress.visibility = View.GONE
    }

    override fun showMessageSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}