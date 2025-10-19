package com.polaris.hospitalitypos.feature.reports.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.hospitalitypos.core.domain.repository.HospitalityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: HospitalityRepository,
    private val pdfExporter: ReportPdfExporter
) : ViewModel() {
    private val _state = MutableStateFlow(ReportsUiState())
    val state: StateFlow<ReportsUiState> = _state

    init {
        _state.value = ReportsUiState(
            reports = listOf(
                ReportItem("Daily Close", "Payments, net/gross by associate"),
                ReportItem("Tax Summary", "Local, state, occupancy taxes"),
                ReportItem("Payment Mix", "Cash vs Card vs Cash App"),
                ReportItem("Incident Summary", "Damage recovery and incidents")
            )
        )
    }

    fun export(report: ReportItem) {
        viewModelScope.launch {
            pdfExporter.export(report)
        }
    }
}

data class ReportsUiState(val reports: List<ReportItem> = emptyList())

data class ReportItem(val title: String, val description: String)
