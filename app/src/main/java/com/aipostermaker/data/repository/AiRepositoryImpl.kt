package com.aipostermaker.data.repository

import com.aipostermaker.data.remote.GeminiRemoteDataSource
import com.aipostermaker.domain.usecase.AiRepository

class AiRepositoryImpl(
    private val remote: GeminiRemoteDataSource,
) : AiRepository {
    override suspend fun generateMarketingText(promptText: String): String {
        return remote.generateMarketingText(promptText)
    }
}

