package com.polaris.hospitalitypos.feature.reservations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polaris.hospitalitypos.core.domain.model.Payment
import com.polaris.hospitalitypos.core.domain.model.PaymentMethod
import com.polaris.hospitalitypos.core.domain.model.PaymentStatus
import com.polaris.hospitalitypos.core.domain.model.Reservation
import com.polaris.hospitalitypos.core.domain.model.ReservationChannel
import com.polaris.hospitalitypos.core.domain.model.ReservationStatus
import com.polaris.hospitalitypos.core.domain.repository.HospitalityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val repository: HospitalityRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ReservationUiState())
    val state: StateFlow<ReservationUiState> = _state

    fun onGuestNameChanged(value: String) {
        _state.value = _state.value.copy(guestName = value).validate()
    }

    fun onRoomChanged(value: String) {
        _state.value = _state.value.copy(roomNumber = value).validate()
    }

    fun onPaymentMethodChanged(value: String) {
        _state.value = _state.value.copy(paymentMethod = value).validate()
    }

    fun confirmReservation() {
        viewModelScope.launch {
            val propertyId = UUID.fromString("11111111-1111-1111-1111-111111111111")
            val reservation = Reservation(
                id = UUID.randomUUID(),
                propertyId = propertyId,
                roomId = UUID.randomUUID(),
                guestId = UUID.randomUUID(),
                checkIn = LocalDate.now(),
                checkOut = LocalDate.now().plusDays(2),
                status = ReservationStatus.CONFIRMED,
                nightlyRate = 150.0,
                balanceDue = 150.0,
                channel = ReservationChannel.DIRECT,
                incidentSummary = null,
                createdAt = java.time.OffsetDateTime.now(),
                updatedAt = java.time.OffsetDateTime.now()
            )
            repository.saveReservation(reservation)
            repository.savePayment(
                Payment(
                    id = UUID.randomUUID(),
                    reservationId = reservation.id,
                    amount = reservation.nightlyRate,
                    method = PaymentMethod.valueOf(_state.value.paymentMethod.ifBlank { PaymentMethod.CARD.name }),
                    status = PaymentStatus.AUTHORIZED,
                    processedAt = java.time.OffsetDateTime.now(),
                    reference = "PENDING",
                    createdAt = java.time.OffsetDateTime.now(),
                    updatedAt = java.time.OffsetDateTime.now()
                )
            )
            _state.value = _state.value.copy(confirmationMessage = "Reservation ${reservation.id} created", isValid = false)
        }
    }
}

data class ReservationUiState(
    val guestName: String = "",
    val roomNumber: String = "",
    val paymentMethod: String = PaymentMethod.CARD.name,
    val confirmationMessage: String = "",
    val isValid: Boolean = true
)

private fun ReservationUiState.validate(): ReservationUiState = copy(
    isValid = guestName.isNotBlank() && roomNumber.isNotBlank() && paymentMethod.isNotBlank()
)
