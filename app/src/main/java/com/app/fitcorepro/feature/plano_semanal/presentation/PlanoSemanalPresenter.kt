package com.app.fitcorepro.feature.plano_semanal.presentation

import com.app.fitcorepro.feature.plano_semanal.presentation.data.PlanoSemanalDataSource
import com.app.fitcorepro.feature.plano_semanal.contract.PlanoSemanalContract
import com.app.fitcorepro.feature.plano_semanal.model.PlanoSemanal

class PlanoSemanalPresenter(
    private val view: PlanoSemanalContract,
    private val dataSource: PlanoSemanalDataSource = PlanoSemanalDataSource()
) : PlanoSemanalCallback {


    fun cadastrarPlanoSemanal(planoSemanal: PlanoSemanal) {
        view.showLoading()
        dataSource.cadastrarPlanoSemanal(planoSemanal, this)
    }


    fun findPlanoSemanalByUsuarioId(usuarioId: String) {
        view.showLoading()
        dataSource.findPlanoSemanalByUsuarioId(usuarioId, this)
    }

    override fun onMessageSuccess(text: String) {
        view.showMessageSuccess(text)
    }

    override fun onSuccess(planoSemanal: PlanoSemanal) {
        view.showPlanoSemanal(planoSemanal)
        view.hideLoading()
    }

    override fun onComplete() {
        view.hideLoading()
    }

    override fun onError(message: String) {
        view.onError(message)
    }

}