package com.app.fitcorepro.feature.dieta_dia.presentation.data

import com.app.fitcorepro.feature.dieta_dia.model.RefeicaoDietaDia
import com.app.fitcorepro.feature.dieta_dia.presentation.BaseDietaDiaCallback
import com.app.fitcorepro.http_conection.HTTPClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import kotlin.collections.set

class RefeicaoDietaDiaDataSource {

        private val PATH_URL = ""

    fun removerRefeicaoDietaDia(refeicaoDietaDiaId: String, callback: BaseDietaDiaCallback) {
        HTTPClient.retrofit(PATH_URL)
            .create(RefeicaoDietaDiaAPI::class.java)
            .removeRefeicao(refeicaoDietaDiaId)
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

    fun addRefeicaoDietaDia(novaRefeicao: RefeicaoDietaDia, callback: BaseDietaDiaCallback) {
        HTTPClient.retrofit(PATH_URL)
            .create(RefeicaoDietaDiaAPI::class.java)
            .addRefeicao(novaRefeicao)
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

    fun updateListRefeicoesDietaDia(refeicoes: List<RefeicaoDietaDia>, callback: BaseDietaDiaCallback) {
        HTTPClient.retrofit(PATH_URL)
            .create(RefeicaoDietaDiaAPI::class.java)
            .updateListRefeicoes(refeicoes)
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
}