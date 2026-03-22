package com.app.fitcorepro.feature.plano_semanal.model

import com.google.gson.annotations.SerializedName

data class ApiMessagemResponse(
    @SerializedName("message")
    var message: String? = null
)