package com.app.fitcorepro.feature.treinamento.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.fitcorepro.R
import com.app.fitcorepro.feature.treinamento.item.TreinoItem
import com.app.fitcorepro.feature.treinamento.model.Exercicio
import com.app.fitcorepro.feature.treinamento.model.Treino
import com.xwray.groupie.GroupieAdapter

class TreinoFragment : Fragment() {

    private val adapter = GroupieAdapter()
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: implementar o presenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_treino, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_treino)
        progress = view.findViewById(R.id.progress_bar_treino)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val exerciciosSegunda = listOf(
            Exercicio(1, "Supino Reto", "4X12"),
            Exercicio(2, "Supino Inclinado", "4X12"),
            Exercicio(3, "Crucifixo", "4X10")
        )

        val exerciciosTerca = listOf(
            Exercicio(4, "puxada costas", "4X12"),
            Exercicio(5, "remada baixa", "4X10"),
            Exercicio(6, "remada alta", "4X10"),
        )

        val exerciciosQuarta = listOf(
            Exercicio(7, "agachamento", "4X12"),
            Exercicio(8, "leg press", "4X12"),
            Exercicio(9, "extensora", "4X12"),
        )

        val treinoSegunda = Treino(1, "Segunda-Feira", exerciciosSegunda)
        val treinoTerca = Treino(2, "Terça-Feira", exerciciosTerca)
        val treinoQuarta = Treino(3, "Quarta-Feira", exerciciosQuarta)

        val listaDeTreinos = listOf(treinoSegunda, treinoTerca, treinoQuarta)

        val itemsParaAdapter = listaDeTreinos.map { treino ->
            TreinoItem(treino)
        }

        adapter.addAll(itemsParaAdapter)
    }
}