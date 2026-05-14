package com.mindmatrix.jalsanchaytracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mindmatrix.jalsanchaytracker.domain.model.CommunityPost
import com.mindmatrix.jalsanchaytracker.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val repository: CommunityRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    val posts: StateFlow<List<CommunityPost>> = repository.observePosts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun publish(description: String, city: String, savedLiters: Double) = viewModelScope.launch {
        val user = auth.currentUser
        repository.publishPost(
            CommunityPost(
                uid = user?.uid.orEmpty(),
                author = user?.email?.substringBefore("@") ?: "Jal Sanchay User",
                city = city,
                setupDescription = description,
                savedLiters = savedLiters
            )
        )
    }
}
