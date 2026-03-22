package com.app.fitcorepro.feature.dieta_dia.contract

import com.app.fitcorepro.feature.dieta_dia.model.DietaDia
import com.app.fitcorepro.feature.dieta_dia.model.RefeicaoDietaDia

interface DietaDiaContract : BaseDietaDiaContract {
    fun navegarParaGerenciarRefeicoes(refeicoes: List<RefeicaoDietaDia>)
    fun exibirRefeicoes(dietaDia: DietaDia)
}