package com.mindmatrix.jalsanchaytracker.domain.model

data class HarvestEntry(
    val id: Long = 0,
    val roofAreaSqm: Double,
    val rainfallMm: Double,
    val runoffCoefficient: Double,
    val tankCapacityLiters: Double,
    val collectedLiters: Double,
    val createdAt: Long = System.currentTimeMillis()
)
