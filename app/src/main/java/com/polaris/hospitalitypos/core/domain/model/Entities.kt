package com.polaris.hospitalitypos.core.domain.model

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class Property(
    val id: UUID,
    val name: String,
    val address: String,
    val phone: String,
    val logoUrl: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val isDeleted: Boolean = false,
    val retentionUntil: OffsetDateTime? = null,
)

data class Room(
    val id: UUID,
    val propertyId: UUID,
    val number: String,
    val type: String,
    val floor: String,
    val isSmoking: Boolean,
    val status: RoomStatus,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val isDeleted: Boolean = false,
    val retentionUntil: OffsetDateTime? = null,
)

enum class RoomStatus { AVAILABLE, BOOKED, DIRTY, OUT_OF_SERVICE }

data class Guest(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val phone: String?,
    val email: String?,
    val loyaltyTier: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val isDeleted: Boolean = false,
    val retentionUntil: OffsetDateTime? = null,
)

data class Reservation(
    val id: UUID,
    val propertyId: UUID,
    val roomId: UUID,
    val guestId: UUID,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val status: ReservationStatus,
    val nightlyRate: Double,
    val balanceDue: Double,
    val channel: ReservationChannel,
    val incidentSummary: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val isDeleted: Boolean = false,
    val retentionUntil: OffsetDateTime? = null,
)

enum class ReservationStatus { HOLD, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED, NO_SHOW }
enum class ReservationChannel { DIRECT, OTA, WALK_IN }

data class Payment(
    val id: UUID,
    val reservationId: UUID,
    val amount: Double,
    val method: PaymentMethod,
    val status: PaymentStatus,
    val processedAt: OffsetDateTime,
    val reference: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

enum class PaymentMethod { CASH, CARD, DEBIT, CASH_APP, PENDING }
enum class PaymentStatus { AUTHORIZED, SETTLED, DECLINED, REFUNDED }

data class TaxProfile(
    val id: UUID,
    val propertyId: UUID,
    val name: String,
    val percentage: Double,
    val type: TaxType,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

enum class TaxType { LOCAL, STATE, OCCUPANCY }

data class User(
    val id: UUID,
    val propertyId: UUID,
    val name: String,
    val email: String,
    val role: Role,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

enum class Role { MANAGER, ASSOCIATE }

data class Template(
    val id: UUID,
    val propertyId: UUID,
    val type: TemplateType,
    val content: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

enum class TemplateType { RECEIPT, TERMS, AUTHORIZATION }

data class GuestIncident(
    val id: UUID,
    val guestId: UUID,
    val propertyId: UUID,
    val description: String,
    val charge: Double?,
    val occurredOn: LocalDate,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

data class AuditLog(
    val id: UUID,
    val userId: UUID,
    val propertyId: UUID,
    val module: AuditModule,
    val action: String,
    val redactedPayload: String,
    val createdAt: OffsetDateTime,
)

enum class AuditModule { AI, RESERVATION, PAYMENT, REPORT, SETTINGS }

data class DailyDigest(
    val propertyId: UUID,
    val occupancy: Double,
    val totalRevenue: Double,
    val paymentMix: Map<PaymentMethod, Double>,
    val arrivals: Int,
    val departures: Int,
    val generatedAt: OffsetDateTime
)
