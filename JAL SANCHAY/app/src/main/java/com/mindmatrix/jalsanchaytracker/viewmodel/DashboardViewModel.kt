package com.mindmatrix.jalsanchaytracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindmatrix.jalsanchaytracker.domain.model.HarvestEntry
import com.mindmatrix.jalsanchaytracker.repository.HarvestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DashboardUiState(
    val savedToday: Double = 0.0,
    val totalSaved: Double = 0.0,
    val householdDays: Double = 0.0,
    val latestEntries: List<HarvestEntry> = emptyList()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    harvestRepository: HarvestRepository
) : ViewModel() {
    val state: StateFlow<DashboardUiState> = combine(
        harvestRepository.observeSavedToday(),
        harvestRepository.observeTotalSaved(),
        harvestRepository.observeEntries()
    ) { today, total, entries ->
        DashboardUiState(
            savedToday = today,
            totalSaved = total,
            householdDays = total / 450.0,
            latestEntries = entries
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())
}
