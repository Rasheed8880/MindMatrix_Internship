package com.mindmatrix.jalsanchaytracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HarvestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: HarvestEntryEntity): Long

    @Query("SELECT * FROM harvest_entries ORDER BY createdAt DESC")
    fun observeEntries(): Flow<List<HarvestEntryEntity>>

    @Query("SELECT COALESCE(SUM(collectedLiters), 0) FROM harvest_entries")
    fun observeTotalSaved(): Flow<Double>

    @Query("SELECT COALESCE(SUM(collectedLiters), 0) FROM harvest_entries WHERE createdAt >= :start AND createdAt < :end")
    fun observeSavedBetween(start: Long, end: Long): Flow<Double>
}
