package com.app.fitcorepro.feature.dieta_dia.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class RefeicaoDietaDia(
    val id: String,
    val titulo: String,
    val ordem: Int,
    val dietaDiaId: String,
    val alimentos: List<AlimentoDietaDia>,
    val createdDate: Date = Date()
) : Parcelable