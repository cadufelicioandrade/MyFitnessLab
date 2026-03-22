package com.app.fitcorepro.feature.dieta_dia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.fitcorepro.feature.dieta_dia.model.RefeicaoDietaDia

class DietaDiaViewModel : ViewModel() {

    private val _novaRefeicaoNome = MutableLiveData<String?>()
    // LiveData público que os Fragments podem observar (mas não modificar)
    val novaRefeicaoNome: LiveData<String?> = _novaRefeicaoNome

    //chamado pelo DialogFragment quando o usuário confirma a adição de uma nova refeição
    fun noNovaRefeicaoAdicionada(nome: String) {
        _novaRefeicaoNome.value = nome
    }

    //chamado pelo DietaDiaFragment depois de consumir o evento
    //para evitar q a mesma refeição seja adicionada novamente (ex: na rotação da tela)
    fun onEventoConsumido() {
        _novaRefeicaoNome.value = null
    }

    private val _refeicoesAtualizadas = MutableLiveData<List<RefeicaoDietaDia>?>()
    val refeicoesAtualizadas: LiveData<List<RefeicaoDietaDia>?> = _refeicoesAtualizadas


    fun onOrdemRefeicoesAtualizada(novaLista: List<RefeicaoDietaDia>) {
        _refeicoesAtualizadas.value = novaLista
    }

    fun onOrdemRefeicoesConsumida() {
        _refeicoesAtualizadas.value = null
    }
}

