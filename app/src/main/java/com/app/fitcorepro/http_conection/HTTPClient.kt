package com.app.fitcorepro.http_conection

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HTTPClient {

    private const val BASE_URL = "http://10.0.2.2:5232/api/"


    private fun httpClient() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY


        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun retrofit(path: String): Retrofit = Retrofit.Builder()
        .baseUrl("${BASE_URL}${path}")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient())
        .build()

}