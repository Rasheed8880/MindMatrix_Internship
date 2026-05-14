package com.aipostermaker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posters")
data class PosterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val contentJson: String,
    val createdAt: Long,
)

