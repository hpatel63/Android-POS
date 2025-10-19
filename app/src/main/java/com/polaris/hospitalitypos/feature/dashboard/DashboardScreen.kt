package com.polaris.hospitalitypos.feature.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.polaris.hospitalitypos.core.domain.model.Property
import com.polaris.hospitalitypos.feature.dashboard.model.KpiCard
import com.polaris.hospitalitypos.feature.dashboard.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(navController: NavHostController, viewModel: DashboardViewModel = hiltViewModel()) {
    val uiState by viewModel.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(listOf(Color(0xAA04111A), Color(0xCC0E2A3B)))
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text("Multi-Property Overview", style = MaterialTheme.typography.displayLarge)
        PropertySelector(properties = uiState.properties, selected = uiState.selectedProperty, onSelect = viewModel::onPropertySelected)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(vertical = 24.dp)) {
            items(uiState.kpis) { card ->
                KpiCardView(card)
            }
        }
        AnimatedVisibility(visible = uiState.isLoadingInsights, enter = fadeIn(), exit = fadeOut()) {
            CircularProgressIndicator()
        }
        uiState.dailyInsight?.let {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x6600C2FF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Daily Insight", style = MaterialTheme.typography.headlineMedium)
                    Text(it, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}

@Composable
private fun PropertySelector(properties: List<Property>, selected: Property?, onSelect: (Property) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(vertical = 16.dp)) {
        properties.forEach { property ->
            AssistChip(onClick = { onSelect(property) }, label = { Text(property.name) }, selected = selected?.id == property.id)
        }
    }
}

@Composable
private fun KpiCardView(card: KpiCard) {
    Card(
        modifier = Modifier
            .height(160.dp)
            .padding(end = 8.dp),
        colors = CardDefaults.cardColors(containerColor = card.tint)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Text(card.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text(card.value, style = MaterialTheme.typography.displayLarge)
            Text(card.delta, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
