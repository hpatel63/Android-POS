package com.polaris.hospitalitypos.feature.housekeeping

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun HousekeepingScreen(navController: NavHostController, viewModel: HousekeepingViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Housekeeping", style = MaterialTheme.typography.displayLarge)
        LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
            items(state.rooms) { room ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text("Room ${room.number} - ${room.status}")
                    Button(onClick = { viewModel.toggleStatus(room.id) }) { Text("Toggle Clean") }
                }
            }
        }
        Text(state.aiSummary, modifier = Modifier.padding(top = 16.dp))
    }
}
