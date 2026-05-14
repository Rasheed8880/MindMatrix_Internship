package com.aipostermaker.domain.usecase

import com.aipostermaker.domain.model.Poster
import kotlinx.coroutines.flow.Flow

class SavePosterUseCase(
    private val posterRepository: PosterRepository,
) {
    suspend operator fun invoke(title: String, contentJson: String, createdAt: Long): Long {
        require(title.isNotBlank()) { "Title cannot be empty" }
        require(contentJson.isNotBlank()) { "Poster content is empty" }
        return posterRepository.insertPoster(title.trim(), contentJson, createdAt)
    }
}

class GetAllPostersUseCase(
    private val posterRepository: PosterRepository,
) {
    operator fun invoke(): Flow<List<Poster>> = posterRepository.getAllPosters()
}

class DeletePosterUseCase(
    private val posterRepository: PosterRepository,
) {
    suspend operator fun invoke(id: Int) {
        posterRepository.deletePoster(id)
    }
}

class GenerateAiTextUseCase(
    private val aiRepository: AiRepository,
) {
    suspend operator fun invoke(businessType: String, offerDetails: String): String {
        require(businessType.isNotBlank()) { "Business type cannot be empty" }
        require(offerDetails.isNotBlank()) { "Offer details cannot be empty" }
        val prompt =
            "Generate one polished marketing poster line for this business: $businessType. Details: $offerDetails. Return only the final poster text, no explanation, no quotes."
        return aiRepository.generateMarketingText(prompt)
    }
}

