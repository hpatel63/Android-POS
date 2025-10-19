package com.polaris.hospitalitypos

import com.polaris.hospitalitypos.core.security.PiiRedactor
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PiiRedactorTest {
    private val redactor = PiiRedactor()

    @Test
    fun `redacts sensitive numbers`() {
        val input = "Guest SSN 123-45-6789 paid with card 4111111111111111"
        val redacted = redactor.redact(input)
        assertFalse(redacted.contains("123-45-6789"))
        assertFalse(redacted.contains("4111111111111111"))
        assertTrue(redacted.contains("**** **** **** ****"))
    }
}
