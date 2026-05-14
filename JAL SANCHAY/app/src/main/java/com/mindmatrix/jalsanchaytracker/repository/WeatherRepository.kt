package com.mindmatrix.jalsanchaytracker.repository

import com.mindmatrix.jalsanchaytracker.network.WeatherApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) {
    suspend fun rainfallForecast(latitude: Double, longitude: Double): List<Pair<String, Double>> {
        val daily = weatherApi.forecast(latitude, longitude).daily ?: return emptyList()
        return daily.time.zip(daily.precipitation_sum)
    }
}
