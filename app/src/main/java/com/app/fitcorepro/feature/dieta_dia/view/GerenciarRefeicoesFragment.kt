package com.app.fitcorepro.feature.dieta_dia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.fitcorepro.R
import com.app.fitcorepro.databinding.FragmentGerenciarRefeicoesDietaDiaBinding // Certifique-se que o nome do XML está correto
import com.app.fitcorepro.feature.dieta_dia.model.RefeicaoDietaDia
import com.app.fitcorepro.feature.dieta_dia.viewmodel.DietaDiaViewModel
import java.util.Collections

class GerenciarRefeicoesFragment : Fragment() {

    private var _binding: FragmentGerenciarRefeicoesDietaDiaBinding? = null
    private val binding get() = _binding!!

    // Usar navArgs() para pegar os argumentos passados pela navegação
    private val args: GerenciarRefeicoesFragmentArgs by navArgs()

    // A lista precisa ser inicializada a partir dos argumentos
    private lateinit var refeicoesList: MutableList<RefeicaoDietaDia>

    private lateinit var adapter: GerenciarRefeicoesAdapter
    private val sharedViewModel: DietaDiaViewModel by activityViewModels()

    //Implementar onCreateView corretamente para usar ViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGerenciarRefeicoesDietaDiaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar a lista com os dados recebidos
        refeicoesList = args.refeicoes.toMutableList().sortedBy { it.ordem }.toMutableList()

        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        //O listener para fechar (navigation icon) deve ser separado
        binding.toolbar.setNavigationOnClickListener {
            // Apenas fecha a tela, sem salvar
            findNavController().popBackStack()
        }

        // Infla o menu que contém o botão "Salvar"
        binding.toolbar.inflateMenu(R.menu.gerenciar_refeicoes_toolbar_menu)

        // O listener para os itens do menu (como "Salvar") é este
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_save) {
                salvarNovaOrdem()
                true // Retorna true para indicar que o clique foi consumido
            } else {
                false
            }
        }
    }

    private fun salvarNovaOrdem() {
        // Atualiza o campo 'ordem' de cada refeição com base na nova posição
        val listaOrdenada = refeicoesList.mapIndexed { index, refeicao ->
            refeicao.copy(ordem = index)
        }

        // Envia a lista atualizada para o ViewModel
        sharedViewModel.onOrdemRefeicoesAtualizada(listaOrdenada)

        // Fecha a tela e volta para o DietaDiaFragment
        findNavController().popBackStack()
    }

    private fun setupRecyclerView() {
        adapter = GerenciarRefeicoesAdapter(refeicoesList)
        binding.rvGerenciarRefeicoes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGerenciarRefeicoes.adapter = adapter

        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.bindingAdapterPosition
                val toPosition = target.bindingAdapterPosition

                if (fromPosition == RecyclerView.NO_POSITION || toPosition == RecyclerView.NO_POSITION) {
                    return false
                }

                Collections.swap(refeicoesList, fromPosition, toPosition)
                adapter.notifyItemMoved(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvGerenciarRefeicoes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// --- ADAPTER --- (Coloquei aqui para simplicidade, pode ser em outro arquivo)

class GerenciarRefeicoesAdapter(private val refeiçoes: List<RefeicaoDietaDia>) :
    RecyclerView.Adapter<GerenciarRefeicoesAdapter.RefeicaoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefeicaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gerenciar_refeicao_dieta_dia, parent, false)
        return RefeicaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RefeicaoViewHolder, position: Int) {
        holder.nome.text = refeiçoes[position].titulo
    }

    override fun getItemCount() = refeiçoes.size

    class RefeicaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.tv_refeicao_nome)
    }
}
