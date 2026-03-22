package com.app.fitcorepro.feature.dieta_dia.presentation.data

import com.app.fitcorepro.feature.dieta_dia.model.AlimentoDietaDia
import retrofit2.Call
import retrofit2.http.*

interface AlimentoBaseDietaDiaAPI {

    @POST
    fun addAlimento(@Body alimentoDietaDia: AlimentoDietaDia): Call<String>

    @PUT
    fun updateAlimento(@Body alimentoDietaDia: AlimentoDietaDia): Call<String>

}