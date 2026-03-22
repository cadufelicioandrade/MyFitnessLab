package com.app.fitcorepro.feature.dieta_dia.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.dieta_dia.item.ItemAlimentoBaseBusca
import com.app.fitcorepro.model.AlimentoBase
import com.google.android.material.textfield.TextInputEditText
import com.xwray.groupie.GroupieAdapter

class ListaAlimentoBaseDietaDiaFragment : Fragment() {

    private val adapter = GroupieAdapter()
    private lateinit var recyclerView: RecyclerView

    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val todosAlimentos = createMockAlimentosBase()
    private lateinit var inputBuscaAlimento: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lista_alimento_base_dieta_dia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputBuscaAlimento = view.findViewById(R.id.input_busca_alimento)
        recyclerView = view.findViewById(R.id.rc_alimentos_base_busca)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        setupSearchInputListener()
        updateRecyclerView(todosAlimentos)
    }

    //ao clicar no item da lista chama essa função
    private fun onAlimentoClicked(alimento: AlimentoBase) {

        val args = ListaAlimentoBaseDietaDiaFragmentArgs.fromBundle(requireArguments())

        //Cria a ação de navegar usando a classe Directions gerada pelo Safe Args
        val action = ListaAlimentoBaseDietaDiaFragmentDirections
            .actionNavListAlimentoRefeicaoDietaDiaToNavAdcionaAlimentoBase(
                // Preenchendo os argumentos que o Safe Args espera
                alimentoId = "",
                alimentoNome = alimento.nome,
                alimentoCalorias = alimento.calorias.toFloat(),
                alimentoCarboidratos = alimento.carboidratos.toFloat(),
                alimentoProteinas = alimento.proteinas.toFloat(),
                alimentoGorduras = alimento.gorduras.toFloat(),
                alimentoFibras = alimento.fibras.toFloat(),
                refeicaoId = args.refeicaoId,
                refeicaoTitulo = args.refeicaoTitulo,
                quantidadeGramas = 0,
                dietaDiaId = args.dietaDiaId
            )

        findNavController().navigate(action)
    }

    //Atualiza o RecyclerView com a lista de alimentos base fornecida
    private fun updateRecyclerView(listaDeAlimentos: List<AlimentoBase>) {
        val itensParaAdapter = listaDeAlimentos.map { alimentoBase ->
            ItemAlimentoBaseBusca(alimentoBase, ::onAlimentoClicked)
        }
        //update faz uma animação suave
        adapter.update(itensParaAdapter)
    }

    //listener para capturar o que é digitado na busca com delay de 500ms
    private fun setupSearchInputListener() {
        inputBuscaAlimento.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    val query = s.toString()
                    Log.d("Search", "Usuário parou de digitar. Buscando por: '$query'")
                    performSearch(query)
                }
                searchHandler.postDelayed(searchRunnable!!, 500)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun performSearch(query: String) {
        val listaFiltrada = if (query.isBlank()) {
            todosAlimentos
        } else {
            todosAlimentos.filter { alimento ->
                alimento.nome.contains(query, ignoreCase = true)
            }
        }
        updateRecyclerView(listaFiltrada)
    }

    private fun createMockAlimentosBase(): List<AlimentoBase> {
        return listOf(
            AlimentoBase("base-01", "Arroz Branco", 100, 130.0, 28.2, 2.7, 0.3, 0.4),
            AlimentoBase("base-02", "Feijão Carioca", 100, 76.0, 13.6, 5.0, 0.5, 8.5),
            AlimentoBase("base-03", "Filé de Frango Grelhado", 100, 165.0, 0.0, 31.0, 3.6, 0.0),
            AlimentoBase("base-04", "Batata Doce Cozida", 100, 77.0, 18.4, 0.6, 0.1, 2.2),
            AlimentoBase("base-05", "Ovo Cozido", 100, 155.0, 1.1, 13.0, 11.0, 0.0),
            AlimentoBase("base-06", "Brócolis Cozido", 100, 35.0, 7.2, 2.5, 0.4, 3.3)
        )
    }
}