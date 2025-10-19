package com.polaris.hospitalitypos.feature.reservations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReservationWizard(viewModel: ReservationViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Guest Information", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = state.guestName, onValueChange = viewModel::onGuestNameChanged, label = { Text("Guest name") }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
        OutlinedTextField(value = state.roomNumber, onValueChange = viewModel::onRoomChanged, label = { Text("Room number") }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
        OutlinedTextField(value = state.paymentMethod, onValueChange = viewModel::onPaymentMethodChanged, label = { Text("Payment method") }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
        Button(onClick = viewModel::confirmReservation, enabled = state.isValid) {
            Text("Confirm Reservation")
        }
        Text(state.confirmationMessage, modifier = Modifier.padding(top = 12.dp))
    }
}
