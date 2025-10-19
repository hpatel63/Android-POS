package com.polaris.hospitalitypos.core.utils

import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateTimeProvider @Inject constructor() {
    fun now(): OffsetDateTime = OffsetDateTime.now()
}
