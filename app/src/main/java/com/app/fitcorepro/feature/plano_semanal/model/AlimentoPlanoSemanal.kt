package com.app.fitcorepro.feature.plano_semanal.model

import java.util.Date

data class AlimentoPlanoSemanal(
    val id: String,
    val nome: String,
    val gramas: Int,
    val refeicaoPlanoSemanalId: String
)