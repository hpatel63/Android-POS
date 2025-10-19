package com.polaris.hospitalitypos.core.domain.usecase

import com.polaris.hospitalitypos.core.domain.model.Reservation
import com.polaris.hospitalitypos.core.domain.model.TaxProfile
import javax.inject.Inject

class CalculateReservationTotals @Inject constructor() {
    operator fun invoke(reservation: Reservation, taxes: List<TaxProfile>): Double {
        val nights = (reservation.checkOut.toEpochDay() - reservation.checkIn.toEpochDay()).coerceAtLeast(1)
        val base = reservation.nightlyRate * nights
        val taxTotal = taxes.sumOf { it.percentage / 100.0 * base }
        return (base + taxTotal).coerceAtLeast(0.0)
    }
}
