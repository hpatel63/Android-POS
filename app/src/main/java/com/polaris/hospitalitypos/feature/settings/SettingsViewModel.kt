package com.polaris.hospitalitypos.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.hospitalitypos.core.ai.SecureConfigStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val secureConfigStore: SecureConfigStore
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsUiState(chatGptKey = secureConfigStore.getApiKey().orEmpty()))
    val state: StateFlow<SettingsUiState> = _state

    fun onBrandNameChanged(value: String) {
        _state.value = _state.value.copy(brandName = value)
    }

    fun onApiKeyChanged(value: String) {
        _state.value = _state.value.copy(chatGptKey = value)
    }

    fun onSuggestiveModeChanged(value: Boolean) {
        _state.value = _state.value.copy(suggestiveMode = value)
    }

    fun save() {
        viewModelScope.launch {
            secureConfigStore.setApiKey(_state.value.chatGptKey)
        }
    }
}

data class SettingsUiState(
    val brandName: String = "Aurora Suites",
    val chatGptKey: String = "",
    val suggestiveMode: Boolean = true
)
