package com.aipostermaker.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GeminiApiService {
    @POST("models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GeminiGenerateContentRequest,
    ): GeminiGenerateContentResponse

    @GET("models")
    suspend fun listModels(
        @Query("key") apiKey: String,
        @Query("pageSize") pageSize: Int = 1000,
        @Query("pageToken") pageToken: String? = null,
    ): GeminiListModelsResponse
}

