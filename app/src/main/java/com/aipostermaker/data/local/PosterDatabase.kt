package com.aipostermaker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PosterEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class PosterDatabase : RoomDatabase() {
    abstract fun posterDao(): PosterDao
}

