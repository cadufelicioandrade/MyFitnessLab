package com.app.fitcorepro.feature.plano_semanal.presentation

import com.app.fitcorepro.feature.plano_semanal.presentation.data.AlimentoDataPlanoSemSource
import com.app.fitcorepro.feature.plano_semanal.contract.AlimentoContract
import com.app.fitcorepro.feature.plano_semanal.model.AlimentoPlanoSemanal

class AlimentoPlanoSemPresenter(
    private val view: AlimentoContract,
    private val dataSource: AlimentoDataPlanoSemSource = AlimentoDataPlanoSemSource()
) : AlimentoPlanoSemCallback {

    fun addListAlimentos(alimentoPlanoSemanals: List<AlimentoPlanoSemanal>) {
        dataSource.addListAlimentos(alimentoPlanoSemanals, this)
    }

    fun removerAlimento(alimentoId: String) {
        view.showLoading()
        dataSource.removerAlimento(alimentoId, this)
    }

    fun editarAlimento(alimentoPlanoSemanal: AlimentoPlanoSemanal) {
        view.showLoading()
        dataSource.updateAlimento(alimentoPlanoSemanal, this)
    }

    override fun showMessageSuccess(message: String) {
        view.showMessageSuccess(message)
    }

    override fun onError(message: String) {
        view.onError(message)
    }

    override fun onComplete() {
        view.hideLoading()
    }
}