package com.polaris.hospitalitypos.feature.housekeeping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.hospitalitypos.core.ai.ChatGptClient
import com.polaris.hospitalitypos.core.domain.model.Room
import com.polaris.hospitalitypos.core.domain.model.RoomStatus
import com.polaris.hospitalitypos.core.domain.repository.HospitalityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HousekeepingViewModel @Inject constructor(
    private val repository: HospitalityRepository,
    private val chatGptClient: ChatGptClient
) : ViewModel() {
    private val _state = MutableStateFlow(HousekeepingUiState())
    val state: StateFlow<HousekeepingUiState> = _state

    init {
        viewModelScope.launch {
            repository.observeProperties().collectLatest { properties ->
                val property = properties.firstOrNull() ?: return@collectLatest
                repository.observeRooms(property.id).collectLatest { rooms ->
                    _state.value = _state.value.copy(rooms = rooms)
                    _state.value = _state.value.copy(
                        aiSummary = chatGptClient.requestInsight("Housekeeping summary for ${rooms.size} rooms")
                    )
                }
            }
        }
    }

    fun toggleStatus(roomId: UUID) {
        viewModelScope.launch {
            val current = _state.value.rooms
            val updated = current.map {
                if (it.id == roomId) it.copy(status = if (it.status == RoomStatus.DIRTY) RoomStatus.AVAILABLE else RoomStatus.DIRTY) else it
            }
            _state.value = _state.value.copy(rooms = updated)
        }
    }
}

data class HousekeepingUiState(
    val rooms: List<Room> = emptyList(),
    val aiSummary: String = "AI summary pending..."
)
