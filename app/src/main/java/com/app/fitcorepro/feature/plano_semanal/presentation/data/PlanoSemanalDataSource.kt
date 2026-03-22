package com.app.fitcorepro.feature.plano_semanal.presentation.data

import com.app.fitcorepro.http_conection.HTTPClient
import com.app.fitcorepro.feature.plano_semanal.model.PlanoSemanal
import com.app.fitcorepro.feature.plano_semanal.presentation.PlanoSemanalCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlanoSemanalDataSource {

    private val PATH_URL = "nutritionplanning/plano-semanal/"

    fun cadastrarPlanoSemanal(planoSemanal: PlanoSemanal, callback: PlanoSemanalCallback) {
        HTTPClient.retrofit(PATH_URL)
            .create(PlanoSemanalAPI::class.java)
            .cadastrarPlanoSemanal(planoSemanal)
            .enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String?>,
                    response: Response<String>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        callback.onMessageSuccess(result.toString())
                    } else {
                        val error = response.errorBody()?.string()
                        callback.onError(error.toString())
                    }
                    callback.onComplete()
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    callback.onError(t.message.toString() ?: "Erro Interno")
                    callback.onComplete()
                }
            })
    }

    fun findPlanoSemanalByUsuarioId(usuarioId: String, callback: PlanoSemanalCallback) {
        HTTPClient.retrofit(PATH_URL)
            .create(PlanoSemanalAPI::class.java)
            .findPlanoSemanalByUsuarioId(usuarioId)
            .enqueue(object : Callback<PlanoSemanal> {
                override fun onResponse(
                    call: Call<PlanoSemanal?>,
                    response: Response<PlanoSemanal?>
                ) {
                    if (response.isSuccessful) {
                        val planoSemanal = response.body()
                        if (planoSemanal != null) {
                            callback.onSuccess(planoSemanal)
                        } else {
                            callback.onError("Resposta vazia")
                        }
                    } else {
                        val error = response.errorBody()?.string()
                        callback.onError(error ?: "Erro na requisição")
                    }
                    callback.onComplete()
                }

                override fun onFailure(
                    call: Call<PlanoSemanal>,
                    t: Throwable
                ) {
                    callback.onError(t.message.toString() ?: "Erro Interno")
                    callback.onComplete()
                }

            })
    }
}