package com.app.fitcorepro.feature.plano_semanal.model

import com.app.fitcorepro.enuns.DiaSemana

data class CriaRefeicaoRequest(
    val planoSemanalDiaId: String,
    val diaSemana: Int,
    val tipoRefeicao: String,
    val alimentoPlanoSemanais: List<AlimentoPlanoSemanal>
)