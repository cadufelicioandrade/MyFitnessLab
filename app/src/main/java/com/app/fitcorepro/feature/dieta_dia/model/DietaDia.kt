package com.app.fitcorepro.feature.dieta_dia.model

import com.app.fitcorepro.feature.dieta_dia.model.RefeicaoDietaDia
import java.time.LocalDate
import java.util.Date

data class DietaDia(
    val id: String,
    val usuarioId: String,
    val dataDieta: LocalDate,
    val refeicoes: List<RefeicaoDietaDia>,
    val createdDate: Date = Date()
)