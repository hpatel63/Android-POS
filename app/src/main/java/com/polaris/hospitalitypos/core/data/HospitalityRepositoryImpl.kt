package com.polaris.hospitalitypos.core.data

import com.polaris.hospitalitypos.core.ai.ChatGptClient
import com.polaris.hospitalitypos.core.data.local.HospitalityDao
import com.polaris.hospitalitypos.core.data.local.toDomain
import com.polaris.hospitalitypos.core.data.remote.HospitalityApi
import com.polaris.hospitalitypos.core.domain.model.DailyDigest
import com.polaris.hospitalitypos.core.domain.model.Payment
import com.polaris.hospitalitypos.core.domain.model.PaymentMethod
import com.polaris.hospitalitypos.core.domain.model.Property
import com.polaris.hospitalitypos.core.domain.model.Reservation
import com.polaris.hospitalitypos.core.domain.model.ReservationChannel
import com.polaris.hospitalitypos.core.domain.model.ReservationStatus
import com.polaris.hospitalitypos.core.domain.model.Room
import com.polaris.hospitalitypos.core.domain.model.User
import com.polaris.hospitalitypos.core.domain.repository.HospitalityRepository
import com.polaris.hospitalitypos.core.security.PiiRedactor
import com.polaris.hospitalitypos.core.utils.DateTimeProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject
import kotlin.math.roundToInt

class HospitalityRepositoryImpl @Inject constructor(
    private val dao: HospitalityDao,
    private val api: HospitalityApi,
    private val chatGptClient: ChatGptClient,
    private val redactor: PiiRedactor,
    private val dateTimeProvider: DateTimeProvider
) : HospitalityRepository {

    override fun observeProperties(): Flow<List<Property>> =
        dao.observeProperties().map { list -> list.map { it.toDomain() } }

    override fun observeRooms(propertyId: UUID): Flow<List<Room>> = dao.observeRooms(propertyId).map { it.map { room -> room.toDomain() } }

    override fun observeReservations(propertyId: UUID): Flow<List<Reservation>> = dao.observeReservations(propertyId).map { it.map { reservation -> reservation.toDomain() } }

    override suspend fun getReservationsForRoom(roomId: UUID, startDate: LocalDate, endDate: LocalDate): List<Reservation> {
        return dao.getReservationsForRoom(roomId, startDate, endDate).map { it.toDomain() }
    }

    override suspend fun saveReservation(reservation: Reservation) {
        dao.upsertReservations(listOf(reservation.toEntity()))
        dao.enqueueSyncJob(reservation.toSyncJob())
    }

    override suspend fun savePayment(payment: Payment) {
        dao.upsertPayments(listOf(payment.toEntity()))
        dao.enqueueSyncJob(payment.toSyncJob())
    }

    override fun observePaymentsForReservation(reservationId: UUID): Flow<List<Payment>> =
        dao.observePaymentsForReservation(reservationId).map { it.map { payment -> payment.toDomain() } }

    override suspend fun fetchRemoteInsights(): Result<String> = runCatching {
        val propertyCount = dao.observeProperties().first().size
        val prompt = redactor.redact("Daily insights requested for $propertyCount properties")
        chatGptClient.requestInsight(prompt)
    }

    override suspend fun syncPendingJobs(): Result<Unit> = runCatching {
        dao.getPendingSyncJobs().forEach { job ->
            when (job.endpoint) {
                "reservations" -> api.updateReservation(job.payload, com.polaris.hospitalitypos.core.data.remote.ReservationRequestDto(
                    propertyId = job.payload,
                    roomId = job.payload,
                    guestId = job.payload,
                    checkIn = dateTimeProvider.now().toLocalDate().toString(),
                    checkOut = dateTimeProvider.now().plusDays(1).toLocalDate().toString(),
                    nightlyRate = 0.0,
                    status = ReservationStatus.CONFIRMED.name,
                    channel = ReservationChannel.DIRECT.name,
                    balanceDue = 0.0
                ))
                "payments" -> api.createPayment(
                    com.polaris.hospitalitypos.core.data.remote.PaymentRequestDto(
                        reservationId = job.payload,
                        amount = 0.0,
                        method = PaymentMethod.CARD.name
                    )
                )
                else -> Unit
            }
            dao.deleteSyncJob(job.id)
        }
    }

    override suspend fun getDailyDigest(propertyId: UUID): DailyDigest {
        val reservations = dao.observeReservations(propertyId).first()
        val occupancy = if (reservations.isEmpty()) 0.0 else reservations.count { it.status == ReservationStatus.CHECKED_IN.name }.toDouble() / reservations.size
        val payments = dao.observePaymentsForProperty(propertyId).first()
        val mix = payments.groupBy { it.method }.mapValues { entry -> entry.value.sumOf { it.amount } }
        return DailyDigest(
            propertyId = propertyId,
            occupancy = (occupancy * 100).roundToInt() / 100.0,
            totalRevenue = payments.sumOf { it.amount },
            paymentMix = PaymentMethod.values().associateWith { method -> mix[method.name] ?: 0.0 },
            arrivals = reservations.count { it.checkIn == LocalDate.now() },
            departures = reservations.count { it.checkOut == LocalDate.now() },
            generatedAt = dateTimeProvider.now()
        )
    }

    override suspend fun currentUser(): User {
        val demoUser = dao.observeProperties()
        return User(
            id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
            propertyId = UUID.fromString("11111111-1111-1111-1111-111111111111"),
            name = "Demo Manager",
            email = "manager@example.com",
            role = com.polaris.hospitalitypos.core.domain.model.Role.MANAGER,
            createdAt = dateTimeProvider.now(),
            updatedAt = dateTimeProvider.now()
        )
    }

    private fun Reservation.toEntity() = com.polaris.hospitalitypos.core.data.local.ReservationEntity(
        id,
        propertyId,
        roomId,
        guestId,
        checkIn,
        checkOut,
        status.name,
        nightlyRate,
        balanceDue,
        channel.name,
        incidentSummary,
        createdAt,
        updatedAt,
        isDeleted,
        retentionUntil
    )

    private fun Reservation.toSyncJob() = com.polaris.hospitalitypos.core.data.local.SyncJobEntity(
        id = UUID.randomUUID(),
        endpoint = "reservations",
        payload = "${id}",
        createdAt = dateTimeProvider.now()
    )

    private fun Payment.toEntity() = com.polaris.hospitalitypos.core.data.local.PaymentEntity(
        id,
        reservationId,
        amount,
        method.name,
        status.name,
        processedAt,
        reference,
        createdAt,
        updatedAt
    )

    private fun Payment.toSyncJob() = com.polaris.hospitalitypos.core.data.local.SyncJobEntity(
        id = UUID.randomUUID(),
        endpoint = "payments",
        payload = "${id}",
        createdAt = dateTimeProvider.now()
    )
}
