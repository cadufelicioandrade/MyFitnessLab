package com.app.fitcorepro.feature.dieta_dia.presentation

interface BaseDietaDiaCallback {
    fun onMessageSuccess(text: String)
    fun onComplete()
    fun onError(message: String)
}