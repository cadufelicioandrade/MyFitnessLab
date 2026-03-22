package com.app.fitcorepro.feature.dieta_dia.presentation.data

import com.app.fitcorepro.feature.dieta_dia.model.AlimentoDietaDia
import com.app.fitcorepro.feature.dieta_dia.presentation.BaseDietaDiaCallback
import com.app.fitcorepro.http_conection.HTTPClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import kotlin.collections.set

class AlimentoBaseDietaDiaDataSource {

    private val PATH_URL = ""

    fun addAlimento(alimentoDietaDia: AlimentoDietaDia, callback: BaseDietaDiaCallback) {
        HTTPClient.retrofit(PATH_URL)
            .create(AlimentoBaseDietaDiaAPI::class.java)
            .addAlimento(alimentoDietaDia)
            .enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String?>,
                    response: Response<String?>
                ) {
                    if (response.isSuccessful) {
                        val msg = response.body().toString()
                        callback.onMessageSuccess(msg)
                    } else {
                        val error = response.errorBody().toString()
                        callback.onError(error)
                    }

                    callback.onComplete()
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    callback.onError(t.message.toString())
                    callback.onComplete()
                }

            })
    }

    fun editAlimento(alimentoDietaDia: AlimentoDietaDia, callback: BaseDietaDiaCallback) {
        HTTPClient.retrofit(PATH_URL)
            .create(AlimentoBaseDietaDiaAPI::class.java)
            .updateAlimento(alimentoDietaDia)
            .enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String?>,
                    response: Response<String?>
                ) {
                    if (response.isSuccessful) {
                        val msg = response.body().toString()
                        callback.onMessageSuccess(msg)
                    } else {
                        val error = response.errorBody().toString()
                        callback.onError(error)
                    }

                    callback.onComplete()
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    callback.onError(t.message.toString())
                    callback.onComplete()
                }

            })
    }

    fun limparAlimentosDasRefeicoes(data: LocalDate) {

    }
}