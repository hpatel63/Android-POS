package com.polaris.hospitalitypos.core.ai

import com.polaris.hospitalitypos.core.data.local.AiPromptLogEntity
import com.polaris.hospitalitypos.core.data.local.HospitalityDao
import com.polaris.hospitalitypos.core.utils.DateTimeProvider
import java.util.UUID
import javax.inject.Inject

class AiLogger @Inject constructor(
    private val dao: HospitalityDao,
    private val dateTimeProvider: DateTimeProvider
) {
    suspend fun logPrompt(module: String, prompt: String, response: String? = null) {
        dao.upsertAiPromptLog(
            AiPromptLogEntity(
                id = UUID.randomUUID(),
                userId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                module = module,
                prompt = prompt,
                response = response,
                createdAt = dateTimeProvider.now()
            )
        )
    }
}
