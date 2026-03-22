package com.app.fitcorepro.feature.plano_semanal.presentation.data

import com.app.fitcorepro.http_conection.HTTPClient
import com.app.fitcorepro.feature.plano_semanal.model.AlimentoPlanoSemanal
import com.app.fitcorepro.feature.plano_semanal.model.ApiMessagemResponse
import com.app.fitcorepro.feature.plano_semanal.presentation.AlimentoPlanoSemCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlimentoDataPlanoSemSource {
    private val PATH_URL = "nutritionplanning/alimento-plano-semanal/"
    fun addListAlimentos(
        alimentoPlanoSemanals: List<AlimentoPlanoSemanal>,
        callback: AlimentoPlanoSemCallback
    ) {
        HTTPClient.retrofit(PATH_URL)
            .create(AlimentoPlanoSemAPI::class.java)
            .addListAlimentos(alimentoPlanoSemanals)
            .enqueue(object : Callback<ApiMessagemResponse> {
                override fun onResponse(
                    call: Call<ApiMessagemResponse>,
                    response: Response<ApiMessagemResponse>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()
                        if (message != null) {
                            callback.showMessageSuccess(message.message ?: "Sucesso!")
                        } else {
                            callback.onError("Resposta vazia do servidor.")
                        }
                    } else {
                        var error = response.errorBody()?.string()
                        callback.onError(error.toString())
                    }
                }

                override fun onFailure(call: Call<ApiMessagemResponse>, t: Throwable) {
                    callback.onError(t.message ?: "Falha de conexão.")
                }
            })
    }

    fun removerAlimento(alimentoId: String, callback: AlimentoPlanoSemCallback) {
        HTTPClient.retrofit(PATH_URL)
            .create(AlimentoPlanoSemAPI::class.java)
            .removerAlimento(alimentoId)
            .enqueue(object : Callback<ApiMessagemResponse> {
                override fun onResponse(
                    call: Call<ApiMessagemResponse>,
                    response: Response<ApiMessagemResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            callback.showMessageSuccess(body.message ?: "Sucesso!")
                        } else {
                            callback.onError("Resposta vazia do servidor.")
                        }
                    } else {
                        var error = response.errorBody()?.string()
                        callback.onError(error.toString())
                    }
                    callback.onComplete()
                }

                override fun onFailure(call: Call<ApiMessagemResponse>, t: Throwable) {
                    callback.onError(t.message ?: "Falha de conexão.")
                    callback.onComplete()
                }
            })
    }

    fun updateAlimento(
        alimentoPlanoSemanal: AlimentoPlanoSemanal,
        callback: AlimentoPlanoSemCallback
    ) {
        HTTPClient.retrofit(PATH_URL)
            .create(AlimentoPlanoSemAPI::class.java)
            .updateAlimento(alimentoPlanoSemanal)
            .enqueue(object : Callback<ApiMessagemResponse> {
                override fun onResponse(
                    call: Call<ApiMessagemResponse>,
                    response: Response<ApiMessagemResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            callback.showMessageSuccess(body.message ?: "Sucesso")
                        } else {
                            callback.onError("Resposta vazia do servidor")
                        }
                    } else {
                        val error = response.errorBody()
                        callback.onError(error.toString())
                    }
                    callback.onComplete()
                }

                override fun onFailure(call: Call<ApiMessagemResponse>, t: Throwable) {
                    callback.onError(t.message ?: "Falha de conexão.")
                    callback.onComplete()
                }
            })

    }
}