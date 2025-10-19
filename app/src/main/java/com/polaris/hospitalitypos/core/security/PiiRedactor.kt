package com.polaris.hospitalitypos.core.security

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PiiRedactor @Inject constructor() {
    fun redact(input: String): String = input
        .replace(Regex("\\b\\d{3}[- ]?\\d{2}[- ]?\\d{4}\\b"), "***-**-****")
        .replace(Regex("\\b[0-9]{16}\\b"), "**** **** **** ****")
        .replace(Regex("\\b\\d{10}\\b"), "(***)***-****")
}
