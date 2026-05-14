package com.mindmatrix.jalsanchaytracker.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mindmatrix.jalsanchaytracker.ui.theme.Aqua
import com.mindmatrix.jalsanchaytracker.ui.theme.Ocean
import com.mindmatrix.jalsanchaytracker.ui.theme.Rain
import com.mindmatrix.jalsanchaytracker.ui.components.JalBackground
import com.mindmatrix.jalsanchaytracker.ui.components.JalLogoHeader

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    JalBackground {
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                JalLogoHeader("Jal-Sanchay Tracker", "Measure every drop with AI")
                Canvas(Modifier.fillMaxWidth().height(260.dp)) {
                    val roof = Path().apply {
                        moveTo(size.width * .12f, size.height * .42f)
                        lineTo(size.width * .5f, size.height * .18f)
                        lineTo(size.width * .88f, size.height * .42f)
                        close()
                    }
                    drawPath(roof, Ocean)
                    drawRect(Aqua, topLeft = Offset(size.width * .25f, size.height * .42f), size = androidx.compose.ui.geometry.Size(size.width * .5f, size.height * .32f))
                    repeat(12) { index ->
                        val x = size.width * (.12f + index * .07f)
                        drawCircle(Rain, radius = 6f, center = Offset(x, size.height * (.08f + (index % 3) * .07f)))
                    }
                    drawCircle(Ocean, radius = 42f, center = Offset(size.width * .5f, size.height * .86f))
                }
                Text("Turn rainfall into measurable water wealth.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Track roof area, rainfall, tank capacity and runoff to calculate saved liters, impact days, and smarter harvesting actions.")
                Text("Use offline history, rain forecasts, AI suggestions, community setups, and reminders to improve every monsoon.")
            }
            Button(onClick = onFinished, modifier = Modifier.fillMaxWidth()) {
                Text("Get Started")
            }
        }
    }
}
