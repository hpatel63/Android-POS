package com.polaris.hospitalitypos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.polaris.hospitalitypos.core.theme.HospitalityTheme
import com.polaris.hospitalitypos.navigation.HospitalityNavHost
import com.polaris.hospitalitypos.feature.offline.ConnectivityBanner
import com.polaris.hospitalitypos.core.work.DailyDigestWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleDigestWorker()
        setContent {
            HospitalityApp()
        }
    }

    private fun scheduleDigestWorker() {
        val request = PeriodicWorkRequestBuilder<DailyDigestWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_digest",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}

@Composable
fun HospitalityApp() {
    HospitalityTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()
            androidx.compose.foundation.layout.Column {
                ConnectivityBanner()
                HospitalityNavHost(navController)
            }
        }
    }
}
