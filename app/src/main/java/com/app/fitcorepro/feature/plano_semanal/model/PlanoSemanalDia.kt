package com.app.fitcorepro.feature.plano_semanal.model

import com.app.fitcorepro.enuns.DiaSemana
import com.app.fitcorepro.feature.plano_semanal.model.RefeicaoPlanoSemanal
import java.util.Date

data class PlanoSemanalDia(
    val id: String,
    val planoSemanaId: String,
    val diaSemana: Int,
    val refeicoes: List<RefeicaoPlanoSemanal>
    )