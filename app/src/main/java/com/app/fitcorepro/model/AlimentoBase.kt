package com.app.fitcorepro.model

data class AlimentoBase(
    val id: String,
    val nome: String,
    val gramas: Int,
    val calorias: Double,
    val carboidratos: Double,
    val proteinas: Double,
    val gorduras: Double,
    val fibras: Double,
)