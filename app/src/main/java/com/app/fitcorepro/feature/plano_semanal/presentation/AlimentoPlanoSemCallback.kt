package com.app.fitcorepro.feature.plano_semanal.presentation

interface AlimentoPlanoSemCallback {
    fun showMessageSuccess(message: String)
    fun onError(message: String)
    fun onComplete()
}