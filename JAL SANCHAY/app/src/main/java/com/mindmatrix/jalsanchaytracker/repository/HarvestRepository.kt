package com.mindmatrix.jalsanchaytracker.repository

import com.mindmatrix.jalsanchaytracker.database.HarvestDao
import com.mindmatrix.jalsanchaytracker.database.toEntity
import com.mindmatrix.jalsanchaytracker.domain.model.HarvestEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HarvestRepository @Inject constructor(
    private val harvestDao: HarvestDao
) {
    fun observeEntries(): Flow<List<HarvestEntry>> =
        harvestDao.observeEntries().map { entries -> entries.map { it.toDomain() } }

    fun observeTotalSaved(): Flow<Double> = harvestDao.observeTotalSaved()

    fun observeSavedToday(): Flow<Double> {
        val zone = ZoneId.systemDefault()
        val start = LocalDate.now().atStartOfDay(zone).toInstant().toEpochMilli()
        val end = LocalDate.now().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
        return harvestDao.observeSavedBetween(start, end)
    }

    suspend fun save(entry: HarvestEntry): Long = harvestDao.insert(entry.toEntity())
}
