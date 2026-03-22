package com.app.fitcorepro.feature.dieta_dia.contract

import com.app.fitcorepro.feature.dieta_dia.model.AlimentoDietaDia
import com.app.fitcorepro.feature.dieta_dia.model.DietaDia
import com.app.fitcorepro.feature.dieta_dia.model.RefeicaoDietaDia

interface BaseDietaDiaContract {
    fun showLoading()
    fun hideLoading()
    fun onMessageSuccess(text: String)
    fun onError(message: String)
}


