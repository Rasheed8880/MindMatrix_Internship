package com.aipostermaker.domain.usecase

import com.aipostermaker.domain.model.Poster
import kotlinx.coroutines.flow.Flow

interface PosterRepository {
    suspend fun insertPoster(title: String, contentJson: String, createdAt: Long): Long
    fun getAllPosters(): Flow<List<Poster>>
    suspend fun deletePoster(id: Int)
}

interface AiRepository {
    suspend fun generateMarketingText(promptText: String): String
}

