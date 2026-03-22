package com.app.fitcorepro.feature.plano_semanal.presentation

import com.app.fitcorepro.feature.plano_semanal.presentation.data.RefeicaoDataPlanoSemSource
import com.app.fitcorepro.feature.plano_semanal.contract.RefeicaoContract
import com.app.fitcorepro.feature.plano_semanal.model.CriaRefeicaoRequest
import com.app.fitcorepro.feature.plano_semanal.model.RefeicaoPlanoSemanal

class RefeicaoPlanoSemPresenter(
    private val view: RefeicaoContract,
    private val dataSource: RefeicaoDataPlanoSemSource = RefeicaoDataPlanoSemSource()
) : RefeicaoCallback {

    fun cadastrarRefeicao(refeicao: CriaRefeicaoRequest) {
        view.showLoading()
        dataSource.cadastrarRefeicao(refeicao, this)
    }

    fun removerRefeicao(refeicaoId: String) {
        view.showLoading()
        dataSource.removerRefeicao(refeicaoId, this)
    }

    override fun onMessageSuccess(text: String) {
        view.showMessageSuccess(text)
    }

    override fun onComplete() {
        view.hideLoading()
    }

    override fun onError(message: String) {
        view.onError(message)
    }
}