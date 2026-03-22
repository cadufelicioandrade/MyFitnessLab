package com.app.fitcorepro.feature.dieta_dia.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class AlimentoDietaDia(
    val id: String,
    val nome: String,
    val refeicaoDietaDiaId: String,
    val quantidadeGrama: Int,
    val calorias: Double,
    val carboidratos: Double,
    val proteinas: Double,
    val gorduras: Double,
    val fibras: Double,
    val createdDate: Date = Date()
) : Parcelable