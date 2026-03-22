package com.app.fitcorepro.feature.plano_semanal.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class RefeicaoPlanoSemanal(
    val id: String,
    val tipo: String,
    val orgem: Int = 0,
    val planoSemanalDiaId: String,
    @SerializedName("alimentoPlanoSemanais")
    val alimentoPlanoSemanais: List<AlimentoPlanoSemanal> = emptyList(),
)