package com.polaris.hospitalitypos.feature.copilot

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.polaris.hospitalitypos.R
import kotlinx.coroutines.launch

@Composable
fun AiAssistantFab(navController: NavHostController, viewModel: CopilotViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    val scope = rememberCoroutineScope()
    val isExpanded = remember { mutableStateOf(false) }
    ExtendedFloatingActionButton(
        onClick = {
            isExpanded.value = !isExpanded.value
            if (isExpanded.value) {
                scope.launch { viewModel.beginStreaming("Provide a performance summary.") }
            }
        },
        text = { Text("AI Copilot") },
        icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_ai_orb), contentDescription = null) },
        containerColor = MaterialTheme.colorScheme.primary
    )
    AnimatedVisibility(visible = isExpanded.value, enter = fadeIn(), exit = fadeOut()) {
        Surface(
            modifier = Modifier
                .padding(bottom = 96.dp)
                .fillMaxWidth(0.4f),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = "AI Assistant", style = MaterialTheme.typography.headlineMedium)
                val transcript by viewModel.transcript.collectAsState()
                Text(text = transcript, modifier = Modifier.padding(top = 12.dp))
            }
        }
    }
}
