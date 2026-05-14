package com.aipostermaker.data.remote

import retrofit2.HttpException

class GeminiRemoteDataSource(
    private val api: GeminiApiService,
    private val apiKey: String,
    private val defaultModel: String,
) {
    private var cachedWorkingModel: String? = null

    suspend fun generateMarketingText(promptText: String): String {
        val request =
            GeminiGenerateContentRequest(
                contents =
                    listOf(
                        GeminiContent(
                            parts = listOf(GeminiPart(text = promptText)),
                        ),
                    ),
            )

        val modelToUse = cachedWorkingModel ?: defaultModel
        val response =
            try {
                api.generateContent(model = modelToUse, apiKey = apiKey, request = request)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    val fallback = findFirstGenerateContentModel()
                    if (fallback != null && fallback != modelToUse) {
                        cachedWorkingModel = fallback
                        return generateMarketingText(promptText)
                    }
                }
                throw IllegalStateException(renderHttpError(e), e)
            }
        val text =
            response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?.trim()
                .orEmpty()

        if (text.isBlank()) {
            throw IllegalStateException("Empty response from AI")
        }
        cachedWorkingModel = modelToUse
        return text
    }

    private suspend fun findFirstGenerateContentModel(): String? {
        var token: String? = null
        repeat(5) {
            val page = api.listModels(apiKey = apiKey, pageSize = 1000, pageToken = token)
            val models = page.models.orEmpty()

            // Prefer current "flash" class models if present
            val preferred =
                models
                    .filter { it.supportedGenerationMethods.orEmpty().any { m -> m == "generateContent" } }
                    .sortedBy { m ->
                        val id = (m.baseModelId ?: m.name).orEmpty()
                        when {
                            id.contains("flash", ignoreCase = true) -> 0
                            id.contains("pro", ignoreCase = true) -> 1
                            else -> 2
                        }
                    }
                    .firstOrNull()

            val candidate = preferred?.baseModelId ?: preferred?.name?.removePrefix("models/")
            if (!candidate.isNullOrBlank()) return candidate

            token = page.nextPageToken
            if (token.isNullOrBlank()) return null
        }
        return null
    }

    private fun renderHttpError(e: HttpException): String {
        val body = e.response()?.errorBody()?.string()?.take(2_000)
        return buildString {
            append("HTTP ")
            append(e.code())
            if (!body.isNullOrBlank()) {
                append(" - ")
                append(body)
            }
        }
    }
}

