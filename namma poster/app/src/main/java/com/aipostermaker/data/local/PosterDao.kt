package com.aipostermaker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PosterDao {
    @Insert
    suspend fun insertPoster(entity: PosterEntity): Long

    @Query("SELECT * FROM posters ORDER BY createdAt DESC")
    fun getAllPosters(): Flow<List<PosterEntity>>

    @Query("DELETE FROM posters WHERE id = :id")
    suspend fun deletePoster(id: Int)
}

