package com.mindmatrix.jalsanchaytracker.repository

import com.mindmatrix.jalsanchaytracker.BuildConfig
import com.mindmatrix.jalsanchaytracker.domain.model.HarvestEntry
import com.mindmatrix.jalsanchaytracker.network.GeminiApi
import com.mindmatrix.jalsanchaytracker.network.GeminiContent
import com.mindmatrix.jalsanchaytracker.network.GeminiPart
import com.mindmatrix.jalsanchaytracker.network.GeminiRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRepository @Inject constructor(
    private val geminiApi: GeminiApi
) {
    suspend fun generateInsights(entries: List<HarvestEntry>, mode: AiInsightMode): String {
        if (BuildConfig.GEMINI_API_KEY.isBlank()) {
            return "Add a Gemini API key in gradle.properties as GEMINI_API_KEY to unlock personalized AI recommendations."
        }
        val recent = entries.take(8).joinToString { "${it.collectedLiters.toInt()} L from ${it.rainfallMm} mm rain" }
        val prompt = mode.prompt(recent.ifBlank { "No saved entries yet" })
        val response = geminiApi.generateContent(
            apiKey = BuildConfig.GEMINI_API_KEY,
            request = GeminiRequest(listOf(GeminiContent(listOf(GeminiPart(prompt)))))
        )
        return response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: "Keep gutters clean, size tanks for peak rain days, and reuse overflow for gardening."
    }

    suspend fun askAssistant(entries: List<HarvestEntry>, question: String): String {
        if (BuildConfig.GEMINI_API_KEY.isBlank()) {
            return "Add a Gemini API key in gradle.properties as GEMINI_API_KEY to unlock Jal AI assistant."
        }
        val recent = entries.take(8).joinToString { "${it.collectedLiters.toInt()} L from ${it.rainfallMm} mm rain" }
        val prompt = """
            You are Jal AI, a helpful chatbot inside Jal-Sanchay Tracker.
            You are the single AI assistant for the whole app. Answer questions about rainwater harvesting, app calculations, runoff coefficient, tank sizing, maintenance, sustainability, optimization plans, setup diagnosis, seasonal advice, community posts, and project explanation.
            User collection history: ${recent.ifBlank { "No saved entries yet" }}.
            User question: $question
            If the user asks for a plan, give clear steps.
            If the user asks for diagnosis, list likely issues and fixes.
            If the user asks for a post, write ready-to-use text.
            If the user asks for project explanation, write student-friendly content.
            Keep the answer simple, friendly, and useful for a student demo.
            Do not use markdown symbols such as **, ###, or table formatting. Use plain text headings and bullet points only.
        """.trimIndent()
        val response = geminiApi.generateContent(
            apiKey = BuildConfig.GEMINI_API_KEY,
            request = GeminiRequest(listOf(GeminiContent(listOf(GeminiPart(prompt)))))
        )
        return response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: "I can help with harvesting calculations, tank capacity, runoff, maintenance, and project explanation."
    }
}

enum class AiInsightMode(val title: String, val buttonLabel: String) {
    TIPS("Smart Tips", "Generate tips"),
    ACTION_PLAN("Optimization Plan", "Create action plan"),
    DIAGNOSIS("System Diagnosis", "Diagnose setup"),
    SEASONAL("Seasonal Forecast", "Seasonal advice"),
    COMMUNITY_POST("Community Post", "Draft post"),
    PROJECT_REPORT("Project Report", "Write summary");

    fun prompt(recentHistory: String): String {
        val base = """
            You are Jal-Sanchay Tracker, an expert GenAI assistant for rainwater harvesting and sustainability.
            User collection history: $recentHistory.
            Keep the answer practical, concise, and suitable for Indian households or student project demos.
        """.trimIndent()
        return when (this) {
            TIPS -> """
                $base
                Generate 5 personalized tips to improve collection, storage, overflow reuse, and maintenance.
            """.trimIndent()
            ACTION_PLAN -> """
                $base
                Create a 7-day optimization plan. Include what to measure, what to clean, how to reduce overflow, and what data to track next.
            """.trimIndent()
            DIAGNOSIS -> """
                $base
                Diagnose likely problems in the harvesting setup. Mention possible causes such as low runoff, undersized tank, dirty gutters, overflow loss, and wrong rainfall data.
                Give each issue a simple fix.
            """.trimIndent()
            SEASONAL -> """
                $base
                Give seasonal recommendations for pre-monsoon, monsoon, post-monsoon, and summer. Include tank safety and recharge planning.
            """.trimIndent()
            COMMUNITY_POST -> """
                $base
                Draft a friendly community feed post where the user shares their rainwater harvesting setup and encourages nearby people to join.
                Keep it under 120 words.
            """.trimIndent()
            PROJECT_REPORT -> """
                $base
                Write a student project report summary explaining how GenAI helps this app beyond normal calculation:
                insights, diagnosis, action planning, seasonal recommendations, community communication, and sustainability education.
            """.trimIndent()
        }
    }
}
