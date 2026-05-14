package com.mindmatrix.jalsanchaytracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindmatrix.jalsanchaytracker.repository.AiInsightMode
import com.mindmatrix.jalsanchaytracker.repository.AiRepository
import com.mindmatrix.jalsanchaytracker.repository.HarvestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatBubble(
    val text: String,
    val fromUser: Boolean
)

data class AiUiState(
    val selectedMode: AiInsightMode = AiInsightMode.TIPS,
    val insight: String = "Choose an AI tool to generate tips, diagnosis, seasonal plans, community text, or project-report content.",
    val chatQuestion: String = "",
    val messages: List<ChatBubble> = listOf(
        ChatBubble(
            "Hi, I am Jal AI. Ask me about rainwater harvesting, tank size, runoff, maintenance, optimization plans, community posts, or project explanation.",
            false
        )
    ),
    val showSuggestions: Boolean = false,
    val isLoading: Boolean = false,
    val isChatLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AiViewModel @Inject constructor(
    private val aiRepository: AiRepository,
    private val harvestRepository: HarvestRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AiUiState())
    val state = _state.asStateFlow()

    fun selectMode(mode: AiInsightMode) {
        _state.value = _state.value.copy(selectedMode = mode, error = null)
    }

    fun updateChatQuestion(value: String) {
        _state.value = _state.value.copy(chatQuestion = value, error = null)
    }

    fun useSuggestedQuestion(question: String) {
        _state.value = _state.value.copy(chatQuestion = question, showSuggestions = false, error = null)
    }

    fun toggleSuggestions() {
        _state.value = _state.value.copy(showSuggestions = !_state.value.showSuggestions)
    }

    fun refresh(mode: AiInsightMode = state.value.selectedMode) = viewModelScope.launch {
        _state.value = _state.value.copy(selectedMode = mode, isLoading = true, error = null)
        try {
            _state.value = _state.value.copy(
                selectedMode = mode,
                insight = aiRepository.generateInsights(harvestRepository.observeEntries().first(), mode),
                isLoading = false
            )
        } catch (error: Exception) {
            _state.value = _state.value.copy(isLoading = false, error = error.message ?: "AI request failed")
        }
    }

    fun askChatbot() = viewModelScope.launch {
        val question = state.value.chatQuestion.trim()
        if (question.length < 3) {
            _state.value = _state.value.copy(error = "Type a question for Jal AI")
            return@launch
        }
        _state.value = _state.value.copy(isChatLoading = true, error = null)
        try {
            val currentMessages = state.value.messages + ChatBubble(question, true)
            _state.value = _state.value.copy(messages = currentMessages, chatQuestion = "")
            val answer = aiRepository.askAssistant(harvestRepository.observeEntries().first(), question)
            _state.value = _state.value.copy(
                messages = currentMessages + ChatBubble(answer, false),
                isChatLoading = false
            )
        } catch (error: Exception) {
            _state.value = _state.value.copy(isChatLoading = false, error = error.message ?: "Chatbot request failed")
        }
    }
}
