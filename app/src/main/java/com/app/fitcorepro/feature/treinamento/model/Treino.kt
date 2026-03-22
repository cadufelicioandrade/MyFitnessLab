package com.app.fitcorepro.feature.treinamento.model

data class Treino(
    val id: Int,
    val diaSemana: String,
    val exercicios: List<Exercicio>
)