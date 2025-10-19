package com.polaris.hospitalitypos.feature.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.polaris.hospitalitypos.core.work.SyncWorker

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    private val monitor: ConnectivityMonitor,
    private val workManager: WorkManager
) : ViewModel() {
    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline

    init {
        viewModelScope.launch {
            monitor.observe().collect {
                _isOnline.value = it
                if (it) {
                    workManager.enqueue(OneTimeWorkRequestBuilder<SyncWorker>().build())
                }
            }
        }
    }
}
