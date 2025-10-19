package com.polaris.hospitalitypos.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.hospitalitypos.core.ai.ChatGptClient
import com.polaris.hospitalitypos.core.domain.repository.HospitalityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: HospitalityRepository,
    private val chatGptClient: ChatGptClient
) : ViewModel() {
    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state
    private var job: Job? = null

    fun onQueryChanged(value: String) {
        _state.value = _state.value.copy(query = value)
        job?.cancel()
        job = viewModelScope.launch {
            _state.value = _state.value.copy(results = listOf("Result for $value"))
            if (value.length > 12) {
                val ai = chatGptClient.requestInsight("Semantic search: $value")
                _state.value = _state.value.copy(results = _state.value.results + ai)
            }
        }
    }
}

data class SearchUiState(
    val query: String = "",
    val results: List<String> = emptyList()
)
