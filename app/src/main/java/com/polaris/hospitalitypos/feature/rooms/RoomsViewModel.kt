package com.polaris.hospitalitypos.feature.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.hospitalitypos.core.domain.model.Reservation
import com.polaris.hospitalitypos.core.domain.model.Room
import com.polaris.hospitalitypos.core.domain.repository.HospitalityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RoomsViewModel @Inject constructor(
    private val repository: HospitalityRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RoomsUiState())
    val state: StateFlow<RoomsUiState> = _state

    init {
        viewModelScope.launch {
            repository.observeProperties().collectLatest { properties ->
                val property = properties.firstOrNull() ?: return@collectLatest
                combine(
                    repository.observeRooms(property.id),
                    repository.observeReservations(property.id)
                ) { rooms, reservations ->
                    RoomsUiState(
                        rooms = rooms,
                        reservationsByRoom = reservations.groupBy { it.roomId }
                    )
                }.collect { uiState -> _state.value = uiState }
            }
        }
    }
}

data class RoomsUiState(
    val rooms: List<Room> = emptyList(),
    val reservationsByRoom: Map<UUID, List<Reservation>> = emptyMap()
)
