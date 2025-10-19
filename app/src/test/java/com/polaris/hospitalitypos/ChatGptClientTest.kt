package com.polaris.hospitalitypos

import com.polaris.hospitalitypos.core.ai.AiLogger
import com.polaris.hospitalitypos.core.ai.ChatGptClient
import com.polaris.hospitalitypos.core.ai.SecureConfigStore
import com.polaris.hospitalitypos.core.data.local.HospitalityDao
import com.polaris.hospitalitypos.core.utils.DateTimeProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatGptClientTest {
    private val dao: HospitalityDao = mockk(relaxed = true)
    private val logger = AiLogger(dao, DateTimeProvider())
    private val store: SecureConfigStore = mockk(relaxed = true)
    private val client = ChatGptClient(store, logger)

    @Test
    fun `stream emits chunks`() = runTest {
        val chunks = client.streamResponse("hello").first()
        assertTrue(chunks.isNotBlank())
    }
}
