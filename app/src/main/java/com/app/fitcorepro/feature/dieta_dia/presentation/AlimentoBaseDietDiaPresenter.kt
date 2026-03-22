package com.app.fitcorepro.feature.dieta_dia.presentation

import com.app.fitcorepro.feature.dieta_dia.contract.BaseDietaDiaContract
import com.app.fitcorepro.feature.dieta_dia.model.AlimentoDietaDia
import com.app.fitcorepro.feature.dieta_dia.presentation.data.AlimentoBaseDietaDiaDataSource

class AlimentoBaseDietDiaPresenter(
    private val viewAlimento: BaseDietaDiaContract,
    private val dataSource: AlimentoBaseDietaDiaDataSource = AlimentoBaseDietaDiaDataSource()
) : BaseDietaDiaCallback {

    fun addAlimento(alimentoDietaDia: AlimentoDietaDia) {
        viewAlimento.showLoading()
        dataSource.addAlimento(alimentoDietaDia, this)
    }

    fun editAlimento(alimentoDietaDia: AlimentoDietaDia) {
        viewAlimento.showLoading()
        dataSource.editAlimento(alimentoDietaDia, this)
    }

    override fun onMessageSuccess(text: String) {
        viewAlimento.hideLoading()
        viewAlimento.onMessageSuccess(text)
    }

    override fun onComplete() {
        viewAlimento.hideLoading()
    }

    override fun onError(message: String) {
        viewAlimento.onError(message)
    }
}