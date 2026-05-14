package com.mindmatrix.jalsanchaytracker.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HarvestEntryEntity::class], version = 1, exportSchema = true)
abstract class JalSanchayDatabase : RoomDatabase() {
    abstract fun harvestDao(): HarvestDao
}
