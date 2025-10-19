package com.polaris.hospitalitypos.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun SearchScreen(navController: NavHostController, viewModel: SearchViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Global Search", style = MaterialTheme.typography.displayLarge)
        OutlinedTextField(value = state.query, onValueChange = viewModel::onQueryChanged, modifier = Modifier.padding(vertical = 12.dp), label = { Text("Search guests, reservations, payments") })
        LazyColumn {
            items(state.results) { result ->
                Text(result, modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}
