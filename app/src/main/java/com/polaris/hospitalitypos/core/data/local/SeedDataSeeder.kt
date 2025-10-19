package com.polaris.hospitalitypos.core.data.local

import com.polaris.hospitalitypos.core.utils.DateTimeProvider
import java.time.LocalDate
import java.util.UUID
import kotlinx.coroutines.flow.firstOrNull

class SeedDataSeeder(
    private val dao: HospitalityDao,
    private val dateTimeProvider: DateTimeProvider
) {
    suspend fun seed() {
        val existing = dao.observeProperties().firstOrNull()?.takeIf { it.isNotEmpty() }
        if (existing != null) return
        val now = dateTimeProvider.now()
        val propertyA = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val propertyB = UUID.fromString("22222222-2222-2222-2222-222222222222")
        dao.upsertProperties(
            listOf(
                PropertyEntity(propertyA, "Aurora Suites", "123 Main St", "555-0100", null, now, now, false, null),
                PropertyEntity(propertyB, "Lumen Lodge", "987 Sunset Blvd", "555-0111", null, now, now, false, null)
            )
        )
        val rooms = (1..10).map { index ->
            RoomEntity(UUID.randomUUID(), propertyA, "10$index", "Double", "1", false, "AVAILABLE", now, now, false, null)
        } + (1..10).map { index ->
            RoomEntity(UUID.randomUUID(), propertyB, "20$index", "King", "2", false, "DIRTY", now, now, false, null)
        }
        dao.upsertRooms(rooms)
        val guest = GuestEntity(UUID.randomUUID(), "Ava", "Rivera", "555-0000", "ava@example.com", "GOLD", now, now, false, null)
        dao.upsertGuests(listOf(guest))
        val reservation = ReservationEntity(
            id = UUID.randomUUID(),
            propertyId = propertyA,
            roomId = rooms.first().id,
            guestId = guest.id,
            checkIn = LocalDate.now(),
            checkOut = LocalDate.now().plusDays(2),
            status = "CONFIRMED",
            nightlyRate = 139.0,
            balanceDue = 278.0,
            channel = "DIRECT",
            incidentSummary = null,
            createdAt = now,
            updatedAt = now,
            isDeleted = false,
            retentionUntil = null
        )
        dao.upsertReservations(listOf(reservation))
        dao.upsertPayments(
            listOf(
                PaymentEntity(
                    id = UUID.randomUUID(),
                    reservationId = reservation.id,
                    amount = 139.0,
                    method = "CARD",
                    status = "SETTLED",
                    processedAt = now,
                    reference = "AUTH123",
                    createdAt = now,
                    updatedAt = now
                )
            )
        )
    }
}
