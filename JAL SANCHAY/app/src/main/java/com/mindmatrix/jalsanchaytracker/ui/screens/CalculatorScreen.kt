package com.mindmatrix.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindmatrix.jalsanchaytracker.ui.components.JalBackground
import com.mindmatrix.jalsanchaytracker.ui.components.JalLogoHeader
import com.mindmatrix.jalsanchaytracker.ui.components.NumberField
import com.mindmatrix.jalsanchaytracker.ui.components.PieChart
import com.mindmatrix.jalsanchaytracker.ui.components.PremiumCard
import com.mindmatrix.jalsanchaytracker.ui.components.TankFill
import com.mindmatrix.jalsanchaytracker.viewmodel.EntryViewModel

@Composable
fun CalculatorScreen(viewModel: EntryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    JalBackground {
        LazyColumn(
            Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                JalLogoHeader("Track water harvesting", "Fill measurements to calculate stored rainwater")
            }
            item { CalculatorTutorialCard() }
            item { NumberField("Roof area (sq.m)", state.roofArea, viewModel::updateRoofArea) }
            item { NumberField("Rainfall (mm)", state.rainfall, viewModel::updateRainfall) }
            item { NumberField("Runoff coefficient", state.runoff, viewModel::updateRunoff) }
            item { NumberField("Tank capacity (L)", state.tankCapacity, viewModel::updateTankCapacity) }
            item { state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            item {
                Button(onClick = viewModel::calculateAndSave, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Calculate, contentDescription = null)
                    Text("Proceed")
                }
            }
            item {
                val result = state.resultLiters
                if (result != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        PremiumCard {
                            Text("Water collected", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("${result.toInt()} L", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                            Text("This is the estimated water stored after applying rainfall, roof area, runoff coefficient, and tank limit.")
                        }
                        TankFill(result, state.tankCapacity.toDoubleOrNull() ?: result)
                        PieChart(result, state.tankCapacity.toDoubleOrNull() ?: result, result)
                    }
                }
            }
        }
    }
}

@Composable
private fun CalculatorTutorialCard() {
    PremiumCard {
        Text("How to fill calculation data", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        TutorialLine("Roof area", "Measure the roof length and width in meters. Multiply them. Example: 10 m x 8 m = 80 sq.m.")
        TutorialLine("Rainfall", "Enter rainfall depth in millimeters. If weather says 25 mm rain, type 25.")
        TutorialLine("Runoff coefficient", "This shows how much rain actually reaches the tank. RCC or metal roofs are usually 0.8 to 0.9. Rough tiles may be 0.6 to 0.75.")
        TutorialLine("Tank capacity", "Enter the maximum storage your tank can hold in liters. Example: 1000 L, 2000 L, 5000 L.")
        Text(
            "Formula: Water collected = Rainfall (mm) x Roof area (sq.m) x Runoff coefficient. Since 1 mm rain on 1 sq.m equals 1 liter, the answer is already in liters.",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            "Example: 25 mm x 80 sq.m x 0.85 = 1700 liters. If your tank is 1000 L, only 1000 L can be stored and the rest becomes overflow.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun TutorialLine(title: String, detail: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(title, modifier = Modifier.weight(0.34f), fontWeight = FontWeight.SemiBold)
        Text(detail, modifier = Modifier.weight(0.66f), style = MaterialTheme.typography.bodyMedium)
    }
}
