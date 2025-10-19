package com.polaris.hospitalitypos.feature.reports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.polaris.hospitalitypos.feature.reports.viewmodel.ReportsViewModel

@Composable
fun ReportsScreen(navController: NavHostController, viewModel: ReportsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Reports", style = MaterialTheme.typography.displayLarge)
        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(state.reports) { report ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(report.title, style = MaterialTheme.typography.titleMedium)
                        Text(report.description, modifier = Modifier.padding(vertical = 8.dp))
                        Button(onClick = { viewModel.export(report) }) {
                            Text("Export PDF")
                        }
                    }
                }
            }
        }
    }
}
