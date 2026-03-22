package com.app.fitcorepro.feature.plano_semanal.presentation.data

import com.app.fitcorepro.feature.plano_semanal.model.ApiMessagemResponse
import com.app.fitcorepro.http_conection.HTTPClient
import com.app.fitcorepro.feature.plano_semanal.model.CriaRefeicaoRequest
import com.app.fitcorepro.feature.plano_semanal.model.RefeicaoPlanoSemanal
import com.app.fitcorepro.feature.plano_semanal.presentation.RefeicaoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RefeicaoDataPlanoSemSource {

    private val PATH_URL = "nutritionplanning/plano-semanal/"

    fun cadastrarRefeicao(refeicao: CriaRefeicaoRequest, callback: RefeicaoCallback) {

        HTTPClient.retrofit(PATH_URL)
            .create(RefeicaoPlanoSemAPI::class.java)
            .adicionarRefeicao(refeicao)
            .enqueue(object : Callback<ApiMessagemResponse> {

                override fun onResponse(
                    call: Call<ApiMessagemResponse?>,
                    response: Response<ApiMessagemResponse>
                ) {
                    handleResponse(response, callback)
                }

                override fun onFailure(
                    call: Call<ApiMessagemResponse>,
                    t: Throwable
                ) {
                    callback.onError(t.message ?: "Falha de conexão")
                    callback.onComplete()
                }
            })
    }

    // ... dentro da classe RefeicaoDataPlanoSemSource

    fun removerRefeicao(refeicaoId: String, callback: RefeicaoCallback) {
        HTTPClient.retrofit(PATH_URL)
            .create(RefeicaoPlanoSemAPI::class.java)
            .removerRefeicao(refeicaoId)
            .enqueue(object : Callback<ApiMessagemResponse> {
                override fun onResponse(
                    call: Call<ApiMessagemResponse>,
                    response: Response<ApiMessagemResponse>
                ) {
                    handleResponse(response, callback)
                }

                override fun onFailure(call: Call<ApiMessagemResponse>, t: Throwable) {
                    callback.onError(t.message ?: "Falha de conexão.")
                    callback.onComplete()
                }
            })
    }

    private fun handleResponse(
        response: Response<ApiMessagemResponse>,
        callback: RefeicaoCallback
    ) {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                callback.onMessageSuccess(body.message ?: "Sucesso")
            } else {
                callback.onError("Resposta vazia do servidor")
            }
        } else {
            val errorJson = response.errorBody()?.string()
            // Tentar extrair a mensagem do JSON de erro se o backend enviar
            callback.onError("Erro na requisição: ${response.code()}")
        }
        callback.onComplete()
    }
}