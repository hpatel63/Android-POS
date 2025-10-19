package com.polaris.hospitalitypos.feature.dashboard.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.hospitalitypos.core.ai.ChatGptClient
import com.polaris.hospitalitypos.core.domain.model.Property
import com.polaris.hospitalitypos.core.domain.repository.HospitalityRepository
import com.polaris.hospitalitypos.feature.dashboard.model.KpiCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: HospitalityRepository,
    private val chatGptClient: ChatGptClient
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state

    init {
        viewModelScope.launch {
            repository.observeProperties().collect { properties ->
                val property = properties.firstOrNull()
                _state.value = _state.value.copy(properties = properties, selectedProperty = property)
                loadKpis(property)
                refreshInsight()
            }
        }
    }

    fun onPropertySelected(property: Property) {
        _state.value = _state.value.copy(selectedProperty = property)
        loadKpis(property)
        viewModelScope.launch { refreshInsight() }
    }

    private fun loadKpis(property: Property?) {
        if (property == null) return
        val occupancy = "78%"
        val adr = "$132"
        val revpar = "$103"
        val paymentMix = "Cash 20% / Card 60% / Cash App 20%"
        _state.value = _state.value.copy(
            kpis = listOf(
                KpiCard("Occupancy", occupancy, "+4% vs WTD", Color(0x4400C2FF)),
                KpiCard("ADR", adr, "+$12 vs LY", Color(0x447A5CFF)),
                KpiCard("RevPAR", revpar, "+$9 vs WTD", Color(0x4495F9C3)),
                KpiCard("Payment Mix", paymentMix, "Balanced", Color(0x44FFA726))
            )
        )
    }

    private suspend fun refreshInsight() {
        _state.value = _state.value.copy(isLoadingInsights = true)
        val insight = chatGptClient.requestInsight("Provide property insights")
        _state.value = _state.value.copy(dailyInsight = insight, isLoadingInsights = false)
    }
}

data class DashboardUiState(
    val properties: List<Property> = emptyList(),
    val selectedProperty: Property? = null,
    val kpis: List<KpiCard> = emptyList(),
    val dailyInsight: String? = null,
    val isLoadingInsights: Boolean = false
)
