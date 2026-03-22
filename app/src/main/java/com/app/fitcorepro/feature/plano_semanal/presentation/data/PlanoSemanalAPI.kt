package com.app.fitcorepro.feature.plano_semanal.presentation.data

import com.app.fitcorepro.feature.plano_semanal.model.PlanoSemanal
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Query

interface PlanoSemanalAPI {

    @POST("plano-semanal")
    fun cadastrarPlanoSemanal(@Body planoSemanal: PlanoSemanal): Call<String>

    @GET("obter-todos/{usuarioId}")
    fun findPlanoSemanalByUsuarioId(@Path("usuarioId") usuarioId: String): Call<PlanoSemanal>

}