package com.mindmatrix.jalsanchaytracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mindmatrix.jalsanchaytracker.domain.model.HarvestEntry

@Entity(tableName = "harvest_entries")
data class HarvestEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val roofAreaSqm: Double,
    val rainfallMm: Double,
    val runoffCoefficient: Double,
    val tankCapacityLiters: Double,
    val collectedLiters: Double,
    val createdAt: Long
) {
    fun toDomain() = HarvestEntry(
        id = id,
        roofAreaSqm = roofAreaSqm,
        rainfallMm = rainfallMm,
        runoffCoefficient = runoffCoefficient,
        tankCapacityLiters = tankCapacityLiters,
        collectedLiters = collectedLiters,
        createdAt = createdAt
    )
}

fun HarvestEntry.toEntity() = HarvestEntryEntity(
    id = id,
    roofAreaSqm = roofAreaSqm,
    rainfallMm = rainfallMm,
    runoffCoefficient = runoffCoefficient,
    tankCapacityLiters = tankCapacityLiters,
    collectedLiters = collectedLiters,
    createdAt = createdAt
)
