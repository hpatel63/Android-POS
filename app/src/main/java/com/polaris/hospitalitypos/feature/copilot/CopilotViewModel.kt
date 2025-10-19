package com.polaris.hospitalitypos.feature.copilot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.hospitalitypos.core.ai.ChatGptClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CopilotViewModel @Inject constructor(
    private val client: ChatGptClient
) : ViewModel() {
    private val _transcript = MutableStateFlow("Ask me anything about your properties.")
    val transcript: StateFlow<String> = _transcript

    fun beginStreaming(prompt: String) {
        viewModelScope.launch {
            client.streamResponse(prompt).collect { chunk ->
                _transcript.value += chunk
            }
        }
    }
}
