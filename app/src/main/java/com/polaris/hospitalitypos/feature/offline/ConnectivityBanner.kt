package com.polaris.hospitalitypos.feature.offline

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ConnectivityBanner(viewModel: ConnectivityViewModel = hiltViewModel()) {
    val isOnline by viewModel.isOnline.collectAsState()
    AnimatedVisibility(
        visible = !isOnline,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Text(
            text = "Offline mode enabled. Changes will sync automatically.",
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}
