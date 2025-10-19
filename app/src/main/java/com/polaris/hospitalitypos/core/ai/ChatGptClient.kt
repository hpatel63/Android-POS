package com.polaris.hospitalitypos.core.ai

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatGptClient @Inject constructor(
    private val secureConfigStore: SecureConfigStore,
    private val aiLogger: AiLogger
) {
    suspend fun requestInsight(prompt: String): String {
        aiLogger.logPrompt("insights", prompt)
        delay(150)
        return "Occupancy is trending upward."
    }

    fun streamResponse(prompt: String): Flow<String> = flow {
        aiLogger.logPrompt("assistant", prompt)
        val chunks = listOf(
            "Analyzing operations...",
            " Occupancy currently at 78% with",
            " 12 arrivals pending housekeeping.",
            " Recommend activating overtime cleaning crew."
        )
        for (chunk in chunks) {
            emit(chunk)
            delay(120)
        }
    }
}
