package com.mindmatrix.jalsanchaytracker.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GeminiApi {
    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    suspend fun generateContent(
        @Header("x-goog-api-key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

data class GeminiRequest(val contents: List<GeminiContent>)
data class GeminiContent(val parts: List<GeminiPart>)
data class GeminiPart(val text: String)

data class GeminiResponse(val candidates: List<GeminiCandidate> = emptyList())
data class GeminiCandidate(val content: GeminiContent? = null)
