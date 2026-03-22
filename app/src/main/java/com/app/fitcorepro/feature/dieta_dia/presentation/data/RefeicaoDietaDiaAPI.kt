package com.app.fitcorepro.feature.dieta_dia.presentation.data

import com.app.fitcorepro.feature.dieta_dia.model.RefeicaoDietaDia
import retrofit2.Call
import retrofit2.http.*


interface RefeicaoDietaDiaAPI {

    @POST("refeicaoDietaDia")
    fun addRefeicao(@Body refeicaoDietaDia: RefeicaoDietaDia): Call<String>

    @DELETE("refeicaoDietaDia/{id}")
    fun removeRefeicao(@Path("id") refeicaoDietaDiaId: String): Call<String>

    @PUT("refeicaoDietaDia")
    fun updateRefeicao(@Body refeicaoDietaDia: RefeicaoDietaDia): Call<String>

    @PUT("refeicaoDietaDia")
    fun updateListRefeicoes(@Body refeicoes: List<RefeicaoDietaDia>): Call<String>
}