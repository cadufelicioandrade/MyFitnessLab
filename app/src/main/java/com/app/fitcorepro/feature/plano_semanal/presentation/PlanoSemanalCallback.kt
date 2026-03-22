package com.app.fitcorepro.feature.plano_semanal.presentation

import com.app.fitcorepro.feature.plano_semanal.model.PlanoSemanal

interface PlanoSemanalCallback {
    fun onMessageSuccess(text: String)
    fun onSuccess(planoSemanal: PlanoSemanal)
    fun onComplete()
    fun onError(message: String)
}