package com.example.myapplication.singleton

import com.example.myapplication.utils.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object WetherObject {
    private val BASE_URL = "https://api.open-meteo.com/v1/"

    val api: WeatherAPIservice by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPIservice::class.java)
    }
}

interface WeatherAPIservice{
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true,
    ): WeatherResponse
}