package com.polaris.hospitalitypos.feature.rooms

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.polaris.hospitalitypos.core.domain.model.Reservation
import com.polaris.hospitalitypos.core.domain.model.Room
import com.polaris.hospitalitypos.feature.reservations.ReservationWizard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoomsScreen(navController: NavHostController, viewModel: RoomsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Room Grid", style = MaterialTheme.typography.displayLarge)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 16.dp)) {
            items(state.rooms) { room ->
                RoomRow(room = room, reservations = state.reservationsByRoom[room.id] ?: emptyList())
            }
        }
        ReservationWizard()
    }
}

@Composable
private fun RoomRow(room: Room, reservations: List<Reservation>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Room ${room.number} • ${room.type}", style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
            items(reservations) { reservation ->
                ReservationCell(reservation)
            }
        }
    }
}

@Composable
private fun ReservationCell(reservation: Reservation) {
    val color = when (reservation.status) {
        com.polaris.hospitalitypos.core.domain.model.ReservationStatus.CONFIRMED -> Color(0xFF1DE9B6)
        com.polaris.hospitalitypos.core.domain.model.ReservationStatus.CHECKED_IN -> Color(0xFFFFC400)
        else -> Color(0xFF42A5F5)
    }
    Surface(
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.6f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${reservation.checkIn} → ${reservation.checkOut}")
            Text("Balance: ${reservation.balanceDue}")
        }
    }
}
