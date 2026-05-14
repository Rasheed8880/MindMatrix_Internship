package com.mindmatrix.jalsanchaytracker.network

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun forecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String = "precipitation_sum",
        @Query("timezone") timezone: String = "auto"
    ): WeatherForecastResponse
}

data class WeatherForecastResponse(
    val daily: DailyForecast? = null
)

data class DailyForecast(
    val time: List<String> = emptyList(),
    val precipitation_sum: List<Double> = emptyList()
)
