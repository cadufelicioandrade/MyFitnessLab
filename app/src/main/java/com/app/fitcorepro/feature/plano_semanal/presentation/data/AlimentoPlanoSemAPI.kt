package com.app.fitcorepro.feature.plano_semanal.presentation.data

import com.app.fitcorepro.feature.plano_semanal.model.AlimentoPlanoSemanal
import com.app.fitcorepro.feature.plano_semanal.model.ApiMessagemResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AlimentoPlanoSemAPI {

    @POST("adiciona-range")
    fun addListAlimentos(@Body alimentoPlanoSemanals: List<AlimentoPlanoSemanal>): Call<ApiMessagemResponse>

    @DELETE("remover/{alimentoId}")
    fun removerAlimento(@Path("alimentoId") alimentoId: String): Call<ApiMessagemResponse>

    @PUT("editar")
    fun updateAlimento(@Body alimentoPlanoSemanal: AlimentoPlanoSemanal): Call<ApiMessagemResponse>

}