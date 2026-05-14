package com.mindmatrix.jalsanchaytracker.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mindmatrix.jalsanchaytracker.domain.model.ChatMessage
import com.mindmatrix.jalsanchaytracker.domain.model.CommunityPost
import com.mindmatrix.jalsanchaytracker.domain.model.UserProfile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun observePosts(): Flow<List<CommunityPost>> = callbackFlow {
        val listener = firestore.collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) close(error)
                else trySend(snapshot?.toObjects(CommunityPost::class.java).orEmpty())
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveProfile(profile: UserProfile) {
        firestore.collection("users").document(profile.uid).set(profile).await()
    }

    suspend fun publishPost(post: CommunityPost) {
        val document = firestore.collection("posts").document()
        document.set(post.copy(id = document.id)).await()
    }

    fun observeChat(chatId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listener = firestore.collection("chats").document(chatId).collection("messages")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) close(error)
                else trySend(snapshot?.toObjects(ChatMessage::class.java).orEmpty())
            }
        awaitClose { listener.remove() }
    }

    suspend fun sendMessage(message: ChatMessage) {
        val document = firestore.collection("chats").document(message.chatId).collection("messages").document()
        document.set(message.copy(id = document.id)).await()
    }
}
