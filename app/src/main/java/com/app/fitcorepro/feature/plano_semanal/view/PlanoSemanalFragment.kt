package com.app.fitcorepro.feature.plano_semanal.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.fitcorepro.R
import com.app.fitcorepro.enuns.DiaSemana
import com.app.fitcorepro.feature.plano_semanal.contract.AlimentoContract
import com.app.fitcorepro.feature.plano_semanal.item.RefeicaoPlanoSemanalItem
import com.app.fitcorepro.feature.plano_semanal.contract.PlanoSemanalContract
import com.app.fitcorepro.feature.plano_semanal.contract.RefeicaoContract
import com.app.fitcorepro.feature.plano_semanal.model.AlimentoPlanoSemanal
import com.app.fitcorepro.feature.plano_semanal.model.PlanoSemanal
import com.app.fitcorepro.feature.plano_semanal.model.RefeicaoPlanoSemanal
import com.app.fitcorepro.feature.plano_semanal.presentation.AlimentoPlanoSemPresenter
import com.app.fitcorepro.feature.plano_semanal.presentation.PlanoSemanalPresenter
import com.app.fitcorepro.feature.plano_semanal.presentation.RefeicaoPlanoSemPresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xwray.groupie.GroupieAdapter

class PlanoSemanalFragment : Fragment(), PlanoSemanalContract, RefeicaoContract, AlimentoContract {

    private val adapter = GroupieAdapter()
    private lateinit var progress: ProgressBar
    private lateinit var presenterPlanoSemanal: PlanoSemanalPresenter
    private lateinit var presenterRefeicao: RefeicaoPlanoSemPresenter
    private lateinit var presenterAlimento: AlimentoPlanoSemPresenter
    private var planoSemanalCompleto: PlanoSemanal? = null
    private lateinit var btnNovoPlano: Button
    private var planoSemanalDiaId: String = ""
    private lateinit var addRefeicaoButton: FloatingActionButton
    private lateinit var txtMsgAddPlano: TextView
    private lateinit var recyclerView: RecyclerView
    private var diaSemanaAtual: DiaSemana = DiaSemana.SEGUNDA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenterPlanoSemanal = PlanoSemanalPresenter(this)
        presenterRefeicao = RefeicaoPlanoSemPresenter(this)
        presenterAlimento = AlimentoPlanoSemPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_plano_semanal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtMsgAddPlano = view.findViewById(R.id.txt_msg_add_plano)
        btnNovoPlano = view.findViewById(R.id.btn_new_plan)
        addRefeicaoButton = view.findViewById(R.id.fab_add_refeicao)
        recyclerView = view.findViewById(R.id.rv_dieta)
        progress = view.findViewById(R.id.progress_bar_dieta)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        setupDayButtons(view)
        setupClicks()

        renderSemPlano()

        presenterPlanoSemanal.findPlanoSemanalByUsuarioId("user123")
    }

    private fun setupClicks() {
        btnNovoPlano.setOnClickListener {
            findNavController().navigate(R.id.action_nav_diet_week_to_nav_new_plan_week)
        }

        addRefeicaoButton.setOnClickListener {
            val bundle = Bundle().apply {
                putString("planoSemanalDiaId", planoSemanalDiaId)
                putString("diaSemanaAtual", diaSemanaAtual.name)
            }
            findNavController().navigate(R.id.action_nav_diet_week_to_nav_new_meal, bundle)
        }
    }

    private fun renderComPlano() {
        txtMsgAddPlano.visibility = View.GONE
        addRefeicaoButton.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE
    }

    private fun renderSemPlano() {
        txtMsgAddPlano.visibility = View.VISIBLE
        addRefeicaoButton.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    fun removerRefeicao(refeicaoPlanoSemanal: RefeicaoPlanoSemanal) {
        presenterRefeicao.removerRefeicao(refeicaoPlanoSemanal.id)
    }

    fun adicionarAlimento(refeicaoPlanoSemanal: RefeicaoPlanoSemanal) {
        val bundle = Bundle().apply {
            putString("refeicaoId", refeicaoPlanoSemanal.id)
            putString("tipoRefeicao", refeicaoPlanoSemanal.tipo)
        }
        findNavController().navigate(R.id.action_nav_diet_week_to_nav_new_aliment, bundle)
    }

    fun editarAlimento(alimentoPlanoSemanal: AlimentoPlanoSemanal) {
        val bundle = Bundle().apply {
            putString("alimentoId", alimentoPlanoSemanal.id)
            putString("alimentoNome", alimentoPlanoSemanal.nome)
            putInt("alimentoGrama", alimentoPlanoSemanal.gramas)
            putString("refeicaoId", alimentoPlanoSemanal.refeicaoPlanoSemanalId)
        }

        findNavController().navigate(R.id.action_nav_diet_week_to_nav_edit_aliment, bundle)
        updateRecyclerViewForDay(diaSemanaAtual)
    }

    fun removerAlimento(alimentoPlanoSemanal: AlimentoPlanoSemanal) {
        presenterAlimento.removerAlimento(alimentoPlanoSemanal.id)
    }

    private fun setupDayButtons(view: View) {
        view.findViewById<Button>(R.id.btn_monday)
            .setOnClickListener { updateRecyclerViewForDay(DiaSemana.SEGUNDA) }
        view.findViewById<Button>(R.id.btn_tuesday)
            .setOnClickListener { updateRecyclerViewForDay(DiaSemana.TERCA) }
        view.findViewById<Button>(R.id.btn_wednesday)
            .setOnClickListener { updateRecyclerViewForDay(DiaSemana.QUARTA) }
        view.findViewById<Button>(R.id.btn_thursday)
            .setOnClickListener { updateRecyclerViewForDay(DiaSemana.QUINTA) }
        view.findViewById<Button>(R.id.btn_friday)
            .setOnClickListener { updateRecyclerViewForDay(DiaSemana.SEXTA) }
        view.findViewById<Button>(R.id.btn_saturday)
            .setOnClickListener { updateRecyclerViewForDay(DiaSemana.SABADO) }
        view.findViewById<Button>(R.id.btn_sunday)
            .setOnClickListener { updateRecyclerViewForDay(DiaSemana.DOMINGO) }
    }

    private fun updateRecyclerViewForDay(day: DiaSemana) {
        diaSemanaAtual = day
        val planoDoDia = planoSemanalCompleto?.planoSemanalDias?.find { it.diaSemana == day.codigo }

        adapter.clear()

        if (planoDoDia != null) {
            planoSemanalDiaId = planoDoDia.id

            val refeicoesParaAdapter = planoDoDia.refeicoes.map { refeicao ->
                RefeicaoPlanoSemanalItem(
                    refeicao,
                    { refeicaoRemover -> removerRefeicao(refeicaoRemover) },
                    { ref -> adicionarAlimento(ref) },
                    { alimentoEdit -> editarAlimento(alimentoEdit) },
                    { alimentoRemover -> removerAlimento(alimentoRemover) }
                )
            }

            adapter.addAll(refeicoesParaAdapter)
        }
    }

    override fun showLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress.visibility = View.GONE
    }

    override fun showMessageSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        presenterPlanoSemanal.findPlanoSemanalByUsuarioId("user123")
    }

    override fun showPlanoSemanal(planoSemanal: PlanoSemanal) {
        planoSemanalCompleto = planoSemanal
        renderComPlano()
        updateRecyclerViewForDay(DiaSemana.SEGUNDA)
    }

    override fun onError(message: String) {
        renderSemPlano()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}