package com.polaris.hospitalitypos.core.data.local

import com.polaris.hospitalitypos.core.domain.model.AuditLog
import com.polaris.hospitalitypos.core.domain.model.AuditModule
import com.polaris.hospitalitypos.core.domain.model.Guest
import com.polaris.hospitalitypos.core.domain.model.GuestIncident
import com.polaris.hospitalitypos.core.domain.model.Payment
import com.polaris.hospitalitypos.core.domain.model.PaymentMethod
import com.polaris.hospitalitypos.core.domain.model.PaymentStatus
import com.polaris.hospitalitypos.core.domain.model.Property
import com.polaris.hospitalitypos.core.domain.model.Reservation
import com.polaris.hospitalitypos.core.domain.model.ReservationChannel
import com.polaris.hospitalitypos.core.domain.model.ReservationStatus
import com.polaris.hospitalitypos.core.domain.model.Role
import com.polaris.hospitalitypos.core.domain.model.Room
import com.polaris.hospitalitypos.core.domain.model.RoomStatus
import com.polaris.hospitalitypos.core.domain.model.TaxProfile
import com.polaris.hospitalitypos.core.domain.model.TaxType
import com.polaris.hospitalitypos.core.domain.model.Template
import com.polaris.hospitalitypos.core.domain.model.TemplateType
import com.polaris.hospitalitypos.core.domain.model.User

fun PropertyEntity.toDomain() = Property(id, name, address, phone, logoUrl, createdAt, updatedAt, isDeleted, retentionUntil)

fun RoomEntity.toDomain() = Room(id, propertyId, number, type, floor, isSmoking, RoomStatus.valueOf(status), createdAt, updatedAt, isDeleted, retentionUntil)

fun GuestEntity.toDomain() = Guest(id, firstName, lastName, phone, email, loyaltyTier, createdAt, updatedAt, isDeleted, retentionUntil)

fun ReservationEntity.toDomain() = Reservation(
    id,
    propertyId,
    roomId,
    guestId,
    checkIn,
    checkOut,
    ReservationStatus.valueOf(status),
    nightlyRate,
    balanceDue,
    ReservationChannel.valueOf(channel),
    incidentSummary,
    createdAt,
    updatedAt,
    isDeleted,
    retentionUntil
)

fun PaymentEntity.toDomain() = Payment(
    id,
    reservationId,
    amount,
    PaymentMethod.valueOf(method),
    PaymentStatus.valueOf(status),
    processedAt,
    reference,
    createdAt,
    updatedAt
)

fun TaxProfileEntity.toDomain() = TaxProfile(id, propertyId, name, percentage, TaxType.valueOf(type), createdAt, updatedAt)

fun UserEntity.toDomain() = User(id, propertyId, name, email, Role.valueOf(role), createdAt, updatedAt)

fun TemplateEntity.toDomain() = Template(id, propertyId, TemplateType.valueOf(type), content, createdAt, updatedAt)

fun GuestIncidentEntity.toDomain() = GuestIncident(id, guestId, propertyId, description, charge, occurredOn, createdAt, updatedAt)

fun AuditLogEntity.toDomain() = AuditLog(id, userId, propertyId, AuditModule.valueOf(module), action, redactedPayload, createdAt)
