package com.mindmatrix.jalsanchaytracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindmatrix.jalsanchaytracker.domain.model.HarvestEntry
import com.mindmatrix.jalsanchaytracker.domain.usecase.CalculateWaterCollectedUseCase
import com.mindmatrix.jalsanchaytracker.repository.HarvestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EntryUiState(
    val roofArea: String = "",
    val rainfall: String = "",
    val runoff: String = "0.85",
    val tankCapacity: String = "",
    val resultLiters: Double? = null,
    val error: String? = null
)

@HiltViewModel
class EntryViewModel @Inject constructor(
    private val harvestRepository: HarvestRepository,
    private val calculateWaterCollected: CalculateWaterCollectedUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EntryUiState())
    val state = _state.asStateFlow()

    fun updateRoofArea(value: String) = update { copy(roofArea = value.filterNumeric(), error = null) }
    fun updateRainfall(value: String) = update { copy(rainfall = value.filterNumeric(), error = null) }
    fun updateRunoff(value: String) = update { copy(runoff = value.filterNumeric(), error = null) }
    fun updateTankCapacity(value: String) = update { copy(tankCapacity = value.filterNumeric(), error = null) }

    fun calculateAndSave() = viewModelScope.launch {
        val current = state.value
        val area = current.roofArea.toDoubleOrNull()
        val rainfall = current.rainfall.toDoubleOrNull()
        val runoff = current.runoff.toDoubleOrNull()
        val tank = current.tankCapacity.toDoubleOrNull()
        if (area == null || rainfall == null || runoff == null || tank == null) {
            update { copy(error = "Fill all fields with valid numbers") }
            return@launch
        }
        if (listOf(area, rainfall, runoff, tank).any { it < 0 }) {
            update { copy(error = "Values cannot be negative") }
            return@launch
        }
        if (runoff > 1.0) {
            update { copy(error = "Runoff coefficient must be between 0 and 1") }
            return@launch
        }
        val liters = calculateWaterCollected(rainfall, area, runoff)
        harvestRepository.save(
            HarvestEntry(
                roofAreaSqm = area,
                rainfallMm = rainfall,
                runoffCoefficient = runoff,
                tankCapacityLiters = tank,
                collectedLiters = liters.coerceAtMost(tank)
            )
        )
        update { copy(resultLiters = liters.coerceAtMost(tank), error = null) }
    }

    private fun update(block: EntryUiState.() -> EntryUiState) {
        _state.value = _state.value.block()
    }

    private fun String.filterNumeric(): String =
        filterIndexed { index, char -> char.isDigit() || (char == '.' && indexOf('.') == index) }
}
