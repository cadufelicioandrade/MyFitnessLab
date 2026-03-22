package com.app.fitcorepro.feature.plano_semanal.contract

interface BaseContrac {
    fun showLoading()
    fun hideLoading()
    fun showMessageSuccess(message: String)
    fun onError(message: String)
}