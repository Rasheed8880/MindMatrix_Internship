package com.mindmatrix.jalsanchaytracker.domain.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val city: String = "",
    val state: String = "",
    val totalSavedLiters: Double = 0.0
)

data class CommunityPost(
    val id: String = "",
    val uid: String = "",
    val author: String = "",
    val city: String = "",
    val setupDescription: String = "",
    val savedLiters: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
