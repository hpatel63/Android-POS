package com.polaris.hospitalitypos.core.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.polaris.hospitalitypos.core.domain.repository.HospitalityRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

@HiltWorker
class DailyDigestWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: HospitalityRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val user = repository.currentUser()
        repository.getDailyDigest(user.propertyId)
        Result.success()
    }
}
