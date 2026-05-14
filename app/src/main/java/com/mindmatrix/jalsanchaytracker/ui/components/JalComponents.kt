package com.mindmatrix.jalsanchaytracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.mindmatrix.jalsanchaytracker.domain.model.HarvestEntry
import com.mindmatrix.jalsanchaytracker.ui.theme.Aqua
import com.mindmatrix.jalsanchaytracker.ui.theme.Clay
import com.mindmatrix.jalsanchaytracker.ui.theme.Leaf
import com.mindmatrix.jalsanchaytracker.ui.theme.Ocean
import com.mindmatrix.jalsanchaytracker.ui.theme.Rain
import kotlin.math.max

@Composable
fun JalBackground(content: @Composable () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFEAF7F4), Color(0xFFF9FBF7), Color(0xFFEFF7FF))
                )
            )
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val topWave = Path().apply {
                moveTo(0f, size.height * 0.06f)
                cubicTo(size.width * .24f, size.height * .02f, size.width * .45f, size.height * .10f, size.width * .68f, size.height * .06f)
                cubicTo(size.width * .84f, size.height * .03f, size.width, size.height * .08f, size.width, size.height * .04f)
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }
            val bottomWave = Path().apply {
                moveTo(0f, size.height)
                lineTo(0f, size.height * .93f)
                cubicTo(size.width * .25f, size.height * .98f, size.width * .52f, size.height * .89f, size.width, size.height * .95f)
                lineTo(size.width, size.height)
                close()
            }
            drawPath(topWave, Brush.horizontalGradient(listOf(Ocean.copy(alpha = .08f), Aqua.copy(alpha = .12f), Color.White.copy(alpha = .55f))))
            drawPath(bottomWave, Brush.horizontalGradient(listOf(Color.White.copy(alpha = .62f), Aqua.copy(alpha = .10f), Rain.copy(alpha = .07f))))
            repeat(18) { index ->
                val x = size.width * ((index * 37 % 100) / 100f)
                val y = size.height * ((index * 19 % 100) / 100f)
                drawLine(
                    color = Ocean.copy(alpha = 0.035f),
                    start = Offset(x, y),
                    end = Offset(x + 8f, y + 18f),
                    strokeWidth = 2f
                )
            }
        }
        content()
    }
}

@Composable
fun JalLogoHeader(title: String, subtitle: String? = null, trailing: (@Composable () -> Unit)? = null) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Brush.linearGradient(listOf(Ocean, Aqua)), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF52645F))
        }
        trailing?.invoke()
    }
}

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.94f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.86f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}

@Composable
fun IconBadge(icon: ImageVector, tint: Color = Ocean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(42.dp)
            .background(tint.copy(alpha = .12f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
    }
}

@Composable
fun CommunityIllustration(modifier: Modifier = Modifier) {
    PremiumCard(modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Canvas(Modifier.size(112.dp, 82.dp)) {
                drawRoundRect(Aqua.copy(alpha = .45f), size = size)
                drawCircle(Ocean, radius = 18f, center = Offset(size.width * .30f, size.height * .34f))
                drawCircle(Leaf, radius = 18f, center = Offset(size.width * .62f, size.height * .30f))
                drawCircle(Clay, radius = 15f, center = Offset(size.width * .47f, size.height * .62f))
                drawLine(Ocean.copy(alpha = .55f), Offset(size.width * .30f, size.height * .34f), Offset(size.width * .62f, size.height * .30f), strokeWidth = 4f)
                drawLine(Ocean.copy(alpha = .40f), Offset(size.width * .47f, size.height * .62f), Offset(size.width * .62f, size.height * .30f), strokeWidth = 4f)
                drawLine(Ocean.copy(alpha = .40f), Offset(size.width * .47f, size.height * .62f), Offset(size.width * .30f, size.height * .34f), strokeWidth = 4f)
            }
            Column(Modifier.weight(1f)) {
                Text("Local water network", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Members, setups, and shared water-saving ideas in one place.", style = MaterialTheme.typography.bodyMedium)
            }
            IconBadge(Icons.Default.Groups, Leaf)
        }
    }
}

@Composable
fun KnowledgeIllustration(modifier: Modifier = Modifier) {
    PremiumCard(modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Canvas(Modifier.size(112.dp, 82.dp)) {
                drawRoundRect(Clay.copy(alpha = .22f), size = size)
                drawRoundRect(Ocean.copy(alpha = .86f), topLeft = Offset(size.width * .12f, size.height * .18f), size = Size(size.width * .32f, size.height * .62f))
                drawRoundRect(Aqua.copy(alpha = .88f), topLeft = Offset(size.width * .45f, size.height * .12f), size = Size(size.width * .18f, size.height * .68f))
                drawRoundRect(Leaf.copy(alpha = .72f), topLeft = Offset(size.width * .65f, size.height * .25f), size = Size(size.width * .22f, size.height * .55f))
                repeat(3) { line ->
                    drawLine(Color.White.copy(alpha = .8f), Offset(size.width * .18f, size.height * (.32f + line * .14f)), Offset(size.width * .37f, size.height * (.32f + line * .14f)), strokeWidth = 3f)
                }
            }
            Column(Modifier.weight(1f)) {
                Text("Learning library", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Guides, basics, and searchable eBook resources for your report.", style = MaterialTheme.typography.bodyMedium)
            }
            IconBadge(Icons.Default.MenuBook, Ocean)
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge, color = color)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun NumberField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun TankFill(collected: Double, capacity: Double, modifier: Modifier = Modifier) {
    val fraction = if (capacity <= 0) 0f else (collected / capacity).coerceIn(0.0, 1.0).toFloat()
    val animated by animateFloatAsState(fraction, label = "tank")
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.size(118.dp, 150.dp)) {
            drawRoundRect(color = Ocean, style = Stroke(width = 5f), size = size)
            val fillHeight = size.height * animated
            drawRoundRect(
                color = Aqua.copy(alpha = 0.82f),
                topLeft = Offset(0f, size.height - fillHeight),
                size = Size(size.width, fillHeight)
            )
            repeat(3) { index ->
                val waveY = size.height - fillHeight + 8f + index * 8f
                if (waveY in 0f..size.height) {
                    drawLine(
                        Color.White.copy(alpha = 0.55f),
                        Offset(12f, waveY),
                        Offset(size.width - 12f, waveY + if (index % 2 == 0) 5f else -5f),
                        strokeWidth = 3f
                    )
                }
            }
        }
        Text("${(fraction * 100).toInt()}%", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PieChart(
    collected: Double,
    capacity: Double,
    totalSaved: Double,
    modifier: Modifier = Modifier
) {
    val stored = collected.coerceAtLeast(0.0)
    val freeSpace = (capacity - stored).coerceAtLeast(0.0)
    val previous = (totalSaved - stored).coerceAtLeast(0.0)
    val total = max(1.0, stored + freeSpace + previous)
    ChartFrame(title = "Water impact split", subtitle = "Stored, free tank space, and previous savings", modifier = modifier) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Canvas(Modifier.size(150.dp)) {
                var start = -90f
                listOf(
                    stored to Ocean,
                    freeSpace to Aqua,
                    previous to Clay
                ).forEach { (value, color) ->
                    val sweep = (value / total * 360f).toFloat()
                    drawArc(color, startAngle = start, sweepAngle = sweep, useCenter = true, size = size)
                    start += sweep
                }
                drawCircle(Color.White, radius = size.minDimension * 0.26f, center = center)
                drawCircle(Ocean.copy(alpha = 0.18f), radius = size.minDimension * 0.48f, center = center, style = Stroke(width = 3f))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LegendLine("Stored now", "${stored.toInt()} L", Ocean)
                LegendLine("Tank space", "${freeSpace.toInt()} L", Aqua)
                LegendLine("Past savings", "${previous.toInt()} L", Clay)
            }
        }
    }
}

@Composable
private fun LegendLine(label: String, value: String, color: Color) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(12.dp).background(color, RoundedCornerShape(3.dp)))
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun LineChart(entries: List<HarvestEntry>, modifier: Modifier = Modifier) {
    val points = entries.take(12).reversed().map { it.collectedLiters }
    ChartFrame(title = "Savings trend", subtitle = "Recent entries connected as a line graph", modifier = modifier) {
        Canvas(Modifier.fillMaxWidth().height(190.dp)) {
            val gridColor = Color(0xFFE0ECE8)
            repeat(5) { index ->
                val y = size.height * index / 4f
                drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 2f)
            }
            repeat(5) { index ->
                val x = size.width * index / 4f
                drawLine(gridColor.copy(alpha = 0.65f), Offset(x, 0f), Offset(x, size.height), strokeWidth = 1.5f)
            }
            if (points.size < 2) {
                drawLine(Ocean.copy(alpha = 0.4f), Offset(0f, size.height * 0.7f), Offset(size.width, size.height * 0.7f), strokeWidth = 5f)
                return@Canvas
            }
            val maxValue = max(1.0, points.max())
            val stepX = size.width / (points.lastIndex)
            val path = Path()
            val fillPath = Path()
            points.forEachIndexed { index, value ->
                val x = index * stepX
                val y = size.height - ((value / maxValue).toFloat() * size.height)
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                if (index == 0) fillPath.moveTo(x, size.height)
                fillPath.lineTo(x, y)
                if (index == points.lastIndex) {
                    fillPath.lineTo(x, size.height)
                    fillPath.close()
                }
            }
            drawPath(fillPath, color = Aqua.copy(alpha = 0.28f), style = Fill)
            drawPath(path, color = Ocean, style = Stroke(width = 7f))
            points.forEachIndexed { index, value ->
                val x = index * stepX
                val y = size.height - ((value / maxValue).toFloat() * size.height)
                drawCircle(Clay, radius = 7f, center = Offset(x, y))
                drawCircle(Color.White, radius = 3.5f, center = Offset(x, y))
            }
        }
    }
}

@Composable
fun BarChart(entries: List<HarvestEntry>, modifier: Modifier = Modifier) {
    val bars = entries.take(7).reversed().map { it.collectedLiters }
    ChartFrame(title = "Daily collection", subtitle = "Bar height shows liters collected", modifier = modifier) {
        Canvas(Modifier.fillMaxWidth().height(190.dp)) {
            val gridColor = Color(0xFFE0ECE8)
            repeat(4) { index ->
                val y = size.height * index / 3f
                drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 2f)
            }
            if (bars.isEmpty()) {
                val sample = listOf(.25f, .48f, .35f, .7f, .55f)
                val gap = 14f
                val barWidth = (size.width - gap * (sample.size - 1)) / sample.size
                sample.forEachIndexed { index, fraction ->
                    val h = size.height * fraction
                    drawRoundRect(
                        color = Ocean.copy(alpha = 0.18f),
                        topLeft = Offset(index * (barWidth + gap), size.height - h),
                        size = Size(barWidth, h)
                    )
                }
                return@Canvas
            }
            val maxValue = max(1.0, bars.max())
            val gap = 12f
            val barWidth = (size.width - gap * (bars.size - 1)) / bars.size
            bars.forEachIndexed { index, value ->
                val h = ((value / maxValue).toFloat() * size.height).coerceAtLeast(6f)
                drawRoundRect(
                    color = when (index % 3) {
                        0 -> Ocean
                        1 -> Leaf
                        else -> Aqua
                    },
                    topLeft = Offset(index * (barWidth + gap), size.height - h),
                    size = Size(barWidth, h)
                )
            }
        }
    }
}

@Composable
private fun ChartFrame(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text("L", color = MaterialTheme.colorScheme.primary)
            }
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF52645F))
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}
