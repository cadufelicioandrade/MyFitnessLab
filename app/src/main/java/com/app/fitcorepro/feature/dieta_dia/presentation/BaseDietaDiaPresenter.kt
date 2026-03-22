package com.app.fitcorepro.feature.dieta_dia.presentation

import com.app.fitcorepro.feature.dieta_dia.contract.BaseDietaDiaContract
import com.app.fitcorepro.feature.dieta_dia.contract.DietaDiaContract
import com.app.fitcorepro.feature.dieta_dia.model.RefeicaoDietaDia
import com.app.fitcorepro.feature.dieta_dia.presentation.data.AlimentoBaseDietaDiaDataSource
import com.app.fitcorepro.feature.dieta_dia.presentation.data.DietaDiaDataSource
import com.app.fitcorepro.feature.dieta_dia.presentation.data.RefeicaoDietaDiaDataSource
import java.time.LocalDate

class BaseDietaDiaPresenter(
    private val view: DietaDiaContract,
    private val dietaDiadataSource: DietaDiaDataSource = DietaDiaDataSource(),
    private val alimentoBaseDietaDiaDataSource: AlimentoBaseDietaDiaDataSource = AlimentoBaseDietaDiaDataSource(),
    private val refeicaoDietaDiaDataSource: RefeicaoDietaDiaDataSource = RefeicaoDietaDiaDataSource()

) : BaseDietaDiaCallback {
    fun carregarDadosParaData(data: LocalDate) {
        val dietaDia = dietaDiadataSource.getDietaDiaByData(data, this)
        view.exibirRefeicoes(dietaDia)
    }

    fun cadastrarNovaRefeicaoDietaDia(nome: String, data: LocalDate) {
        val dietaAtual = dietaDiadataSource.getDietaDiaByData(data, this)
        val proximaOrdem = dietaAtual.refeicoes.size
        val novaRefeicao = RefeicaoDietaDia(
            id = "user_added_${System.currentTimeMillis()}",
            titulo = nome,
            ordem = proximaOrdem,
            dietaDiaId = dietaAtual.id,
            alimentos = emptyList()
        )

        refeicaoDietaDiaDataSource.addRefeicaoDietaDia(novaRefeicao, this)

        val dietaAtualizada = dietaDiadataSource.getDietaDiaByData(data, this)
        view.exibirRefeicoes(dietaAtualizada)
        view.onMessageSuccess("Refeição ${nome} cadastrada com sucesso!")

    }

    fun salvarNovaOrdemRefeicoesDietaDia(data: LocalDate, refeicoes: List<RefeicaoDietaDia>) {
        // Apenas delega o salvamento para o DataSource
        refeicaoDietaDiaDataSource.updateListRefeicoesDietaDia(refeicoes, this)
    }

    fun limparRefeicoes(data: LocalDate) {
        alimentoBaseDietaDiaDataSource.limparAlimentosDasRefeicoes(data)

        // Após limpar, recarrega os dados para a view refletir a mudança
        val dietaAtualizada = dietaDiadataSource.getDietaDiaByData(data, this)
        view.exibirRefeicoes(dietaAtualizada)
        view.onMessageSuccess("Refeições do dia limpas!")
    }

    fun iniciarFluxoGerenciarRefeicoes(data: LocalDate) {
        val dietaDia = dietaDiadataSource.getDietaDiaByData(data, this)
        if (dietaDia.refeicoes.isNotEmpty()) {
            // Manda a View (Fragment) navegar com os dados corretos
            view.navegarParaGerenciarRefeicoes(dietaDia.refeicoes)
        } else {
            view.onError("Não há refeições cadastradas para gerenciar.")
        }
    }

    fun removerRefeicaoDietaDia(refeicaoDietaDiaId: String) {
        view.showLoading()
        refeicaoDietaDiaDataSource.removerRefeicaoDietaDia(refeicaoDietaDiaId, this)
    }

    override fun onMessageSuccess(text: String) {
        view.onMessageSuccess(text)
    }

    override fun onComplete() {
        view.hideLoading()
    }

    override fun onError(message: String) {
        view.onError(message)
    }

}
