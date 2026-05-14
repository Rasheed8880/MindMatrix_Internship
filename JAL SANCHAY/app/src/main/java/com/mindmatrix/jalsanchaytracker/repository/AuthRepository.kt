package com.mindmatrix.jalsanchaytracker.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mindmatrix.jalsanchaytracker.domain.model.UserProfile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    fun observeLoggedIn(): Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser != null) }
        auth.addAuthStateListener(listener)
        trySend(auth.currentUser != null)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun login(email: String, password: String) {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user ?: return
        firestore.collection("users").document(user.uid).set(
            mapOf(
                "uid" to user.uid,
                "email" to (user.email ?: email),
                "lastLoginAt" to System.currentTimeMillis()
            ),
            SetOptions.merge()
        ).await()
    }

    suspend fun register(
        name: String,
        email: String,
        phone: String,
        city: String,
        password: String
    ) {
        val existingPhone = firestore.collection("users")
            .whereEqualTo("phone", phone)
            .limit(1)
            .get()
            .await()
        if (!existingPhone.isEmpty) {
            throw IllegalArgumentException("This phone number is already registered")
        }

        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: return
        firestore.collection("users").document(uid).set(
            UserProfile(
                uid = uid,
                name = name,
                email = email,
                phone = phone,
                city = city,
                state = "",
                totalSavedLiters = 0.0
            ).asFirestoreMap()
        ).await()
    }

    fun logout() = auth.signOut()
}

private fun UserProfile.asFirestoreMap(): Map<String, Any> = mapOf(
    "uid" to uid,
    "name" to name,
    "email" to email,
    "phone" to phone,
    "city" to city,
    "state" to state,
    "totalSavedLiters" to totalSavedLiters,
    "createdAt" to System.currentTimeMillis(),
    "lastLoginAt" to System.currentTimeMillis()
)
