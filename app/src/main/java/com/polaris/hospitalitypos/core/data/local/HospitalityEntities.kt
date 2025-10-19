package com.polaris.hospitalitypos.core.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey val id: UUID,
    val name: String,
    val address: String,
    val phone: String,
    @ColumnInfo(name = "logo_url") val logoUrl: String?,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: OffsetDateTime,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "retention_until") val retentionUntil: OffsetDateTime?
)

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "property_id") val propertyId: UUID,
    val number: String,
    val type: String,
    val floor: String,
    @ColumnInfo(name = "is_smoking") val isSmoking: Boolean,
    val status: String,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: OffsetDateTime,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "retention_until") val retentionUntil: OffsetDateTime?
)

@Entity(tableName = "guests")
data class GuestEntity(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    val phone: String?,
    val email: String?,
    @ColumnInfo(name = "loyalty_tier") val loyaltyTier: String?,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: OffsetDateTime,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "retention_until") val retentionUntil: OffsetDateTime?
)

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "property_id") val propertyId: UUID,
    @ColumnInfo(name = "room_id") val roomId: UUID,
    @ColumnInfo(name = "guest_id") val guestId: UUID,
    @ColumnInfo(name = "check_in") val checkIn: LocalDate,
    @ColumnInfo(name = "check_out") val checkOut: LocalDate,
    val status: String,
    @ColumnInfo(name = "nightly_rate") val nightlyRate: Double,
    @ColumnInfo(name = "balance_due") val balanceDue: Double,
    val channel: String,
    @ColumnInfo(name = "incident_summary") val incidentSummary: String?,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: OffsetDateTime,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "retention_until") val retentionUntil: OffsetDateTime?
)

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "reservation_id") val reservationId: UUID,
    val amount: Double,
    val method: String,
    val status: String,
    @ColumnInfo(name = "processed_at") val processedAt: OffsetDateTime,
    val reference: String?,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: OffsetDateTime
)

@Entity(tableName = "tax_profiles")
data class TaxProfileEntity(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "property_id") val propertyId: UUID,
    val name: String,
    val percentage: Double,
    val type: String,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: OffsetDateTime
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "property_id") val propertyId: UUID,
    val name: String,
    val email: String,
    val role: String,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: OffsetDateTime
)

@Entity(tableName = "templates")
data class TemplateEntity(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "property_id") val propertyId: UUID,
    val type: String,
    val content: String,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: OffsetDateTime
)

@Entity(tableName = "guest_incidents")
data class GuestIncidentEntity(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "guest_id") val guestId: UUID,
    @ColumnInfo(name = "property_id") val propertyId: UUID,
    val description: String,
    val charge: Double?,
    @ColumnInfo(name = "occurred_on") val occurredOn: LocalDate,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: OffsetDateTime
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "user_id") val userId: UUID,
    @ColumnInfo(name = "property_id") val propertyId: UUID,
    val module: String,
    val action: String,
    @ColumnInfo(name = "redacted_payload") val redactedPayload: String,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime
)

@Entity(tableName = "sync_queue")
data class SyncJobEntity(
    @PrimaryKey val id: UUID,
    val endpoint: String,
    val payload: String,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "retry_count") val retryCount: Int = 0
)

@Entity(tableName = "manual_articles")
data class ManualArticleEntity(
    @PrimaryKey val id: UUID,
    val title: String,
    val body: String,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime
)

@Entity(tableName = "ai_prompts")
data class AiPromptLogEntity(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "user_id") val userId: UUID,
    val module: String,
    val prompt: String,
    val response: String?,
    @ColumnInfo(name = "created_at") val createdAt: OffsetDateTime
)


data class ReservationWithRelations(
    @Embedded val reservation: ReservationEntity,
    @Relation(parentColumn = "guest_id", entityColumn = "id")
    val guest: GuestEntity,
    @Relation(parentColumn = "room_id", entityColumn = "id")
    val room: RoomEntity,
    @Relation(parentColumn = "property_id", entityColumn = "id")
    val property: PropertyEntity
)
