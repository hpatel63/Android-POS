package com.polaris.hospitalitypos.feature.dashboard.model

import androidx.compose.ui.graphics.Color

data class KpiCard(
    val title: String,
    val value: String,
    val delta: String,
    val tint: Color
)
