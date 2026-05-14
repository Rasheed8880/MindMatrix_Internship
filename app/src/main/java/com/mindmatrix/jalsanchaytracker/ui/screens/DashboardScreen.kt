package com.mindmatrix.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindmatrix.jalsanchaytracker.ui.components.BarChart
import com.mindmatrix.jalsanchaytracker.ui.components.JalBackground
import com.mindmatrix.jalsanchaytracker.ui.components.JalLogoHeader
import com.mindmatrix.jalsanchaytracker.ui.components.LineChart
import com.mindmatrix.jalsanchaytracker.ui.components.MetricCard
import com.mindmatrix.jalsanchaytracker.ui.components.PieChart
import com.mindmatrix.jalsanchaytracker.ui.components.TankFill
import com.mindmatrix.jalsanchaytracker.ui.theme.Clay
import com.mindmatrix.jalsanchaytracker.ui.theme.Leaf
import com.mindmatrix.jalsanchaytracker.ui.theme.Ocean
import com.mindmatrix.jalsanchaytracker.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    JalBackground {
        LazyColumn(
            Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                JalLogoHeader("Dashboard", "Water saved, impact, and tank status") {
                    IconButton(onClick = onLogout) { Icon(Icons.Default.Logout, contentDescription = "Logout") }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    MetricCard("Today", "${state.savedToday.toInt()} L", "saved liters", Ocean, Modifier.weight(1f))
                    MetricCard("Total", "${state.totalSaved.toInt()} L", "all-time", Leaf, Modifier.weight(1f))
                }
            }
            item { MetricCard("Impact score", "%.1f days".format(state.householdDays), "household water days", Clay) }
            item {
                val latest = state.latestEntries.firstOrNull()
                TankFill(latest?.collectedLiters ?: 0.0, latest?.tankCapacityLiters ?: 1000.0)
            }
            item {
                val latest = state.latestEntries.firstOrNull()
                PieChart(latest?.collectedLiters ?: 0.0, latest?.tankCapacityLiters ?: 1000.0, state.totalSaved)
            }
            item { LineChart(state.latestEntries) }
            item { BarChart(state.latestEntries) }
        }
    }
}
