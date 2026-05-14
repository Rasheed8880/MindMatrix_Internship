package com.aipostermaker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aipostermaker.domain.model.Poster
import com.aipostermaker.domain.usecase.DeletePosterUseCase
import com.aipostermaker.domain.usecase.GetAllPostersUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PosterListViewModel(
    getAllPostersUseCase: GetAllPostersUseCase,
    private val deletePosterUseCase: DeletePosterUseCase,
) : ViewModel() {
    val posters: StateFlow<List<Poster>> =
        getAllPostersUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun deletePoster(id: Int) {
        viewModelScope.launch {
            deletePosterUseCase(id)
        }
    }
}

