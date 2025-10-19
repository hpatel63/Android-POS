package com.polaris.hospitalitypos.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(navController: NavHostController, viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Settings", style = MaterialTheme.typography.displayLarge)
        OutlinedTextField(
            value = state.brandName,
            onValueChange = viewModel::onBrandNameChanged,
            label = { Text("Brand Name") },
            modifier = Modifier.padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = state.chatGptKey,
            onValueChange = viewModel::onApiKeyChanged,
            label = { Text("ChatGPT API Key") },
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Switch(checked = state.suggestiveMode, onCheckedChange = viewModel::onSuggestiveModeChanged)
        Button(onClick = viewModel::save) { Text("Save") }
    }
}
