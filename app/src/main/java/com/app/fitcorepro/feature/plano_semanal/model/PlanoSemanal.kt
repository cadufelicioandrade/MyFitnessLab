package com.app.fitcorepro.feature.plano_semanal.model

import java.util.Date

data class PlanoSemanal(
    val id: String,
    val nome: String,
    val ativo: Boolean,
    val usuarioId: String,
    val planoSemanalDias: List<PlanoSemanalDia>
)