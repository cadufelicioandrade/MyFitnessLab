package com.app.fitcorepro.feature.plano_semanal.presentation

interface RefeicaoCallback {
    fun onMessageSuccess(text: String)
    fun onComplete()
    fun onError(message: String)

}