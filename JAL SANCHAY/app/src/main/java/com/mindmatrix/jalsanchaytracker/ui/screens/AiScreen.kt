package com.mindmatrix.jalsanchaytracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindmatrix.jalsanchaytracker.ui.components.IconBadge
import com.mindmatrix.jalsanchaytracker.ui.components.JalBackground
import com.mindmatrix.jalsanchaytracker.ui.components.JalLogoHeader
import com.mindmatrix.jalsanchaytracker.ui.components.PremiumCard
import com.mindmatrix.jalsanchaytracker.viewmodel.ChatBubble
import com.mindmatrix.jalsanchaytracker.viewmodel.AiViewModel

private val aiSuggestions = listOf(
    "Create an optimization plan for my rainwater harvesting setup.",
    "Diagnose possible problems in my roof, runoff, tank, or overflow setup.",
    "Give seasonal maintenance advice for pre-monsoon, monsoon, post-monsoon, and summer.",
    "Explain the rainwater harvesting formula in simple words for my project viva.",
    "Draft a community post inviting people to join Jal Circle.",
    "Write a short project report summary explaining how Jal AI helps."
)

@Composable
fun AiScreen(viewModel: AiViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    JalBackground {
        Column(
            Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            JalLogoHeader("Jal AI", "Interactive assistant for every water question")
            Box(Modifier.weight(1f)) {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.messages) { message ->
                        ChatBubbleView(message)
                    }
                    item {
                        if (state.isChatLoading) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator()
                                Text("Jal AI is thinking...")
                            }
                        }
                    }
                }
                if (state.showSuggestions) {
                    SuggestionsPopup(
                        onPick = viewModel::useSuggestedQuestion,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            PremiumCard {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = viewModel::toggleSuggestions) {
                        Icon(Icons.Default.Lightbulb, contentDescription = "Suggestions", tint = MaterialTheme.colorScheme.primary)
                    }
                    OutlinedTextField(
                        value = state.chatQuestion,
                        onValueChange = viewModel::updateChatQuestion,
                        label = { Text("Message Jal AI") },
                        modifier = Modifier.weight(1f),
                        minLines = 1,
                        maxLines = 3
                    )
                    IconButton(onClick = viewModel::askChatbot, enabled = !state.isChatLoading) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubbleView(message: ChatBubble) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.fromUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .background(
                    if (message.fromUser) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = .96f),
                    RoundedCornerShape(8.dp)
                )
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (!message.fromUser) IconBadge(Icons.Default.SmartToy, MaterialTheme.colorScheme.primary, Modifier)
                Text(
                    if (message.fromUser) "You" else "Jal AI",
                    color = if (message.fromUser) Color.White else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                renderSimpleMarkdown(message.text),
                color = if (message.fromUser) Color.White else Color(0xFF1C2B27)
            )
        }
    }
}

@Composable
private fun SuggestionsPopup(onPick: (String) -> Unit, modifier: Modifier = Modifier) {
    PremiumCard(modifier.padding(bottom = 8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text("Try asking", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        aiSuggestions.forEach { suggestion ->
            Button(onClick = { onPick(suggestion) }, modifier = Modifier.fillMaxWidth()) {
                Text(suggestion)
            }
        }
    }
}

private fun renderSimpleMarkdown(raw: String) = buildAnnotatedString {
    val cleaned = raw
        .replace("###", "")
        .replace("##", "")
        .replace("#", "")
        .replace("* ", "• ")
        .replace(Regex("""(?m)^\s*\*\s*"""), "• ")
    var index = 0
    while (index < cleaned.length) {
        val start = cleaned.indexOf("**", index)
        if (start == -1) {
            append(cleaned.substring(index).replace("**", ""))
            break
        }
        append(cleaned.substring(index, start))
        val end = cleaned.indexOf("**", start + 2)
        if (end == -1) {
            append(cleaned.substring(start + 2).replace("**", ""))
            break
        }
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(cleaned.substring(start + 2, end).replace("**", ""))
        }
        index = end + 2
    }
}
