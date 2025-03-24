package com.example.myapplication.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit{
    val baseURL= "http://192.168.118.253:8080/"
    val client= OkHttpClient.Builder()
        .addInterceptor(JWTs()).build()
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(baseURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}



