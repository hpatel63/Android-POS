package com.polaris.hospitalitypos

import com.polaris.hospitalitypos.core.domain.model.Reservation
import com.polaris.hospitalitypos.core.domain.model.ReservationChannel
import com.polaris.hospitalitypos.core.domain.model.ReservationStatus
import com.polaris.hospitalitypos.core.domain.model.TaxProfile
import com.polaris.hospitalitypos.core.domain.model.TaxType
import com.polaris.hospitalitypos.core.domain.usecase.CalculateReservationTotals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

class CalculateReservationTotalsTest {
    private val useCase = CalculateReservationTotals()

    @Test
    fun `calculates totals with taxes`() {
        val reservation = Reservation(
            id = UUID.randomUUID(),
            propertyId = UUID.randomUUID(),
            roomId = UUID.randomUUID(),
            guestId = UUID.randomUUID(),
            checkIn = LocalDate.of(2024, 3, 1),
            checkOut = LocalDate.of(2024, 3, 4),
            status = ReservationStatus.CONFIRMED,
            nightlyRate = 100.0,
            balanceDue = 0.0,
            channel = ReservationChannel.DIRECT,
            incidentSummary = null,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val taxes = listOf(
            TaxProfile(UUID.randomUUID(), reservation.propertyId, "State", 5.0, TaxType.STATE, OffsetDateTime.now(), OffsetDateTime.now()),
            TaxProfile(UUID.randomUUID(), reservation.propertyId, "Local", 3.0, TaxType.LOCAL, OffsetDateTime.now(), OffsetDateTime.now())
        )

        val total = useCase(reservation, taxes)

        assertEquals(318.0, total, 0.01)
    }
}
