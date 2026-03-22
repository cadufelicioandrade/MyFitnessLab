package com.app.fitcorepro.feature.dieta_dia.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.dieta_dia.item.RefeicaoDietaDiaItem
import com.app.fitcorepro.feature.dieta_dia.model.AlimentoDietaDia
import com.app.fitcorepro.feature.dieta_dia.model.DietaDia
import com.app.fitcorepro.feature.dieta_dia.model.RefeicaoDietaDia
import com.app.fitcorepro.feature.dieta_dia.viewmodel.DietaDiaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import com.app.fitcorepro.databinding.FragmentDietaDiaBinding
import com.app.fitcorepro.feature.dieta_dia.contract.DietaDiaContract
import com.app.fitcorepro.feature.dieta_dia.presentation.BaseDietaDiaPresenter
import com.xwray.groupie.GroupieAdapter

class DietaDiaFragment : Fragment(), DietaDiaContract {

    private val adapter = GroupieAdapter()
    private var dataAtual: LocalDate = LocalDate.now()
    private var _binding: FragmentDietaDiaBinding? = null
    private val binding get() = _binding!!

    //activityViewModels() é de instância do ViewModel compartilhado entre DietaDiaFragment e AddRefeicaoDietaDiaDialogFragment
    //resultado não precisa de interface/callback para compartilhar dados entre os fragmentos
    private val sharedViewModel: DietaDiaViewModel by activityViewModels()
    private lateinit var dietaDiaPresenter: BaseDietaDiaPresenter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dietaDiaPresenter = BaseDietaDiaPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietaDiaBinding.inflate(inflater, container, false)

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRefeicao.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRefeicao.adapter = adapter
        setupDateNavigation()

        //cria um listener para o ImagemButon para o gerenciar refeições
        binding.navDietaDiaInclude.btnOpcoesRefeicao.setOnClickListener { viewAnchor ->
            //ao ser clicado abre o menu
            showOptionsMenu(viewAnchor)
        }

        binding.fabAddRefeicao.setOnClickListener {
            //cria uma instância do AddRefeicaoDietaDiaDialogFragment e chama o .show() para mostrar o diálog
            //parentFragmentManager é um gerenciador do fragmento pai para exibir o BottomSheetDialogFragment
            //por cima da tela atual como um modal, AddRefeicaoDialog e´só um identificador se quiser encontrar esse dialog depois
            AddRefeicaoDietaDiaDialogFragment().show(parentFragmentManager, "AddRefeicaoDialog")
        }

        observeNovaRefeicao()
        observeOrdenacaoRefeicoes()
        carregarDadosParaData(dataAtual)
    }

    private fun observeOrdenacaoRefeicoes() {
        sharedViewModel.refeicoesAtualizadas.observe(viewLifecycleOwner) { listaAtualizada ->
            if (listaAtualizada != null) {
                dietaDiaPresenter.salvarNovaOrdemRefeicoesDietaDia(dataAtual, listaAtualizada)
                atualizarAdapterComLista(listaAtualizada)
                sharedViewModel.onOrdemRefeicoesConsumida()
            }
        }
    }

    private fun observeNovaRefeicao() {
        sharedViewModel.novaRefeicaoNome.observe(viewLifecycleOwner) { nomeRefeicao ->
            if (!nomeRefeicao.isNullOrEmpty()) {
                dietaDiaPresenter.cadastrarNovaRefeicaoDietaDia(nomeRefeicao, dataAtual)
                sharedViewModel.onEventoConsumido()
            }
        }
    }

    private fun showOptionsMenu(anchorView: View) {
        val popup = PopupMenu(requireContext(), anchorView)
        popup.menuInflater.inflate(R.menu.dieta_dia_options_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_gerenciar_refeicoes -> {

                    dietaDiaPresenter.iniciarFluxoGerenciarRefeicoes(dataAtual)
                    true
                }

                R.id.menu_limpar_refeicoes -> {
                    limparRefeicoesDoDia()
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    private fun limparRefeicoesDoDia() {
        AlertDialog.Builder(requireContext())
            .setTitle("Limpar Refeições")
            .setMessage("Tem certeza que deseja remover todos os alimentos das refeições deste dia?")
            .setPositiveButton("Sim, limpar") { _, _ ->
                dietaDiaPresenter.limparRefeicoes(dataAtual)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    //Lógica de Navegação botões << >> entre os dias
    private fun setupDateNavigation() {
        binding.navDietaDiaInclude.btnNavLeft.setOnClickListener {
            dataAtual = dataAtual.minusDays(1)
            carregarDadosParaData(dataAtual)
        }

        binding.navDietaDiaInclude.btnNavRight.setOnClickListener {
            dataAtual = dataAtual.plusDays(1)
            carregarDadosParaData(dataAtual)
        }

        binding.navDietaDiaInclude.dtDietaDia.setOnClickListener {
            mostrarDatePicker()
        }
    }

    //Ao clicar na data abre o calendário para selecionar um dia especifico
    private fun mostrarDatePicker() {
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val dataSelecionada = LocalDate.of(year, month + 1, dayOfMonth)
                if (dataSelecionada != dataAtual) {
                    dataAtual = dataSelecionada
                    carregarDadosParaData(dataAtual)
                }
            },
            dataAtual.year,
            dataAtual.monthValue - 1,
            dataAtual.dayOfMonth
        )
        datePicker.show()
    }

    private fun carregarDadosParaData(data: LocalDate) {
        val formatador = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        binding.navDietaDiaInclude.dtDietaDia.text = data.format(formatador)
        dietaDiaPresenter.carregarDadosParaData(data)
    }


    // Renomeie para refletir que ela recebe uma lista como parâmetro
    private fun atualizarAdapterComLista(refeicoes: List<RefeicaoDietaDia>) {
        adapter.clear()

        // Ordena a lista pelo campo 'ordem' antes de exibi-la    val refeicoesOrdenadas = refeicoes.sortedBy { it.ordem }

        val refeicaoAdapterItems = refeicoes.map { refeicao ->
            RefeicaoDietaDiaItem(
                refeicao,
                { r -> removeRefeicao(r) },
                { r -> adicionarAlimento(r) },
                { a -> editarAlimento(a) },
                { a -> removeAlimento(a) }
            )
        }

        adapter.addAll(refeicaoAdapterItems)
    }


    private fun updateHeaderMacros(refeicoes: List<RefeicaoDietaDia>) {
        if (refeicoes.isEmpty()) {
            binding.headerMacrosIncluce.carboidrato.text = String.format("Carb: %.1f g", 0.0)
            binding.headerMacrosIncluce.proteina.text = String.format("Prot: %.1f g", 0.0)
            binding.headerMacrosIncluce.gordura.text = String.format("Gord: %.1f g", 0.0)
            binding.headerMacrosIncluce.fibra.text = String.format("Fib: %.1f g", 0.0)
            binding.headerMacrosIncluce.Kcal.text = String.format("Kcal: %d", 0)
            return
        }

        val todosAlimentos = refeicoes.flatMap { it.alimentos }
        val totalCarb = todosAlimentos.sumOf { it.carboidratos }
        val totalProt = todosAlimentos.sumOf { it.proteinas }
        val totalGord = todosAlimentos.sumOf { it.gorduras }
        val totalFibra = todosAlimentos.sumOf { it.fibras }
        val totalKcal = todosAlimentos.sumOf { it.calorias }

        binding.headerMacrosIncluce.carboidrato.text = String.format("Carb: %.1f g", totalCarb)
        binding.headerMacrosIncluce.proteina.text = String.format("Prot: %.1f g", totalProt)
        binding.headerMacrosIncluce.gordura.text = String.format("Gord: %.1f g", totalGord)
        binding.headerMacrosIncluce.fibra.text = String.format("Fib: %.1f g", totalFibra)
        binding.headerMacrosIncluce.Kcal.text = String.format("Kcal: %.0f", totalKcal)
    }

    // Funções de callback para revemover refeição
    private fun removeRefeicao(refeicaoDietaDia: RefeicaoDietaDia) {
        dietaDiaPresenter.removerRefeicaoDietaDia(refeicaoDietaDia.id)
    }

    // Esta função será chamada pelo clique no botão '+' DENTRO de um card de refeição
    private fun adicionarAlimento(refeicaoDietaDia: RefeicaoDietaDia) {
        try {
            val navController = findNavController()

            val action = DietaDiaFragmentDirections
                .actionNavDietDiaToNavAddAlimentoRefeicaoDietaDia(
                    refeicaoDietaDia.id,
                    refeicaoDietaDia.titulo,
                    refeicaoDietaDia.dietaDiaId
                )
            // Navega para o destino especificado
            navController.navigate(action)
        } catch (e: IllegalStateException) {
            Log.e("DietaDiaFragment", "Não foi possível navegar para adicionar alimento.", e)
            Toast.makeText(
                requireContext(),
                "Erro de navegação. Tente novamente.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun editarAlimento(alimentoDietaDia: AlimentoDietaDia) {

        try {
            val navController = findNavController()

            val action = DietaDiaFragmentDirections
                .actionNavDietDiaToNavAcoesAlimentoBaseDietaDia(
                    alimentoDietaDia.id,
                    alimentoDietaDia.nome,
                    alimentoDietaDia.calorias.toFloat(),
                    alimentoDietaDia.carboidratos.toFloat(),
                    alimentoDietaDia.proteinas.toFloat(),
                    alimentoDietaDia.gorduras.toFloat(),
                    alimentoDietaDia.fibras.toFloat(),
                    alimentoDietaDia.quantidadeGrama,
                    alimentoDietaDia.refeicaoDietaDiaId,
                    "",
                    ""
                )
            navController.navigate(action)
        } catch (e: IllegalStateException) {
            Log.e(
                "DietaDiaFragment",
                "Não foi possível navegar: o fragmento não está anexado ou o NavController não foi encontrado.",
                e
            )
            Toast.makeText(
                requireContext(),
                "Erro de navegação. Tente novamente.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun removeAlimento(alimentoDietaDia: AlimentoDietaDia) {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun navegarParaGerenciarRefeicoes(refeicoes: List<RefeicaoDietaDia>) {
        val action =
            DietaDiaFragmentDirections.actionNavDietDiaToNavGerenciarRefeicoes(
                refeicoes.toTypedArray()
            )
        findNavController().navigate(action)
    }


    override fun showLoading() {
        //depois implementar a espera
    }

    override fun hideLoading() {
        //depois implementar a espera
    }


    override fun onMessageSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun exibirRefeicoes(dietaDia: DietaDia) {
        atualizarAdapterComLista(dietaDia.refeicoes)
        updateHeaderMacros(dietaDia.refeicoes)
    }
}