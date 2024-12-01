package com.example.myapplication.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class WeatherResponse(
    val weather: WeatherData?,
    val npk: NPKValues?
)


data class NPKValues(
    val n: Double,
    val p: Double,
    val k: Double,
    val ph: Double
)

data class WeatherData(
    val latitude: Double,
    val longitude: Double,
    val generationtime_ms: String,  // Keep exact field names
    val utc_offset_seconds: Int,
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Double,
    val current_units: CurrentUnitsDTO, // Keep exact field names
    val current: CurrentWeatherDTO
) {
    data class CurrentUnitsDTO(
        val time: String,
        val interval: String,
        val temperature_2m: String,  // Exact field names
        val relative_humidity_2m: String,
        val rain: String,
        val weather_code: String,
        val wind_speed_10m: String,
        val wind_direction_10m: String
    )

    data class CurrentWeatherDTO(
        val time: String,
        val interval: Int,
        val temperature_2m: Double,
        val relative_humidity_2m: Int,
        val rain: Double,
        val weather_code: Int,
        val wind_speed_10m: Double,
        val wind_direction_10m: Double
    )
}


@Parcelize
data class CurrentWeather(
    val time: String,          // e.g., "2024-11-24T06:30"
    val temperature: Double,   // e.g., 26.9 (°C)
    val windspeed: Double,     // e.g., 10.8 (km/h)
    val winddirection: Int,    // e.g., 88 (degrees)
    val is_day: Int,           // e.g., 1 (daytime)
    val weathercode: Int       // e.g., 2 (weather code)
) :Parcelable


@Parcelize
data class CurrentWeatherUnits(
    val time: String,          // "iso8601"
    val interval: String,      // "seconds"
    val temperature: String,   // "°C"
    val windspeed: String,     // "km/h"
    val winddirection: String, // "°"
    val is_day: String,        // Can be empty
    val weathercode: String    // "wmo code"
): Parcelable

