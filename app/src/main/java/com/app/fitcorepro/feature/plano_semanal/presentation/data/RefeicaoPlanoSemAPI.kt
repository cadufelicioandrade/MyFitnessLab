package com.app.fitcorepro.feature.plano_semanal.presentation.data

import com.app.fitcorepro.feature.plano_semanal.model.ApiMessagemResponse
import com.app.fitcorepro.feature.plano_semanal.model.CriaRefeicaoRequest
import retrofit2.Call
import retrofit2.http.*

interface RefeicaoPlanoSemAPI {

    @POST("refeicao")
    fun adicionarRefeicao(@Body refeicao: CriaRefeicaoRequest): Call<ApiMessagemResponse>

    @DELETE("delete-refeicao/{idRefeicao}")
    fun removerRefeicao(@Path("idRefeicao") id: String): Call<ApiMessagemResponse>

}