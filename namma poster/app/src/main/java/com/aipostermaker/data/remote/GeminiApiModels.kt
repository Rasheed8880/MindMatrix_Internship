package com.aipostermaker.data.remote

import com.google.gson.annotations.SerializedName

data class GeminiGenerateContentRequest(
    @SerializedName("contents") val contents: List<GeminiContent>,
)

data class GeminiContent(
    @SerializedName("parts") val parts: List<GeminiPart>,
)

data class GeminiPart(
    @SerializedName("text") val text: String,
)

data class GeminiGenerateContentResponse(
    @SerializedName("candidates") val candidates: List<GeminiCandidate>?,
)

data class GeminiCandidate(
    @SerializedName("content") val content: GeminiContent?,
)

data class GeminiListModelsResponse(
    @SerializedName("models") val models: List<GeminiModel>?,
    @SerializedName("nextPageToken") val nextPageToken: String?,
)

data class GeminiModel(
    @SerializedName("name") val name: String?,
    @SerializedName("baseModelId") val baseModelId: String?,
    @SerializedName("supportedGenerationMethods") val supportedGenerationMethods: List<String>?,
)

