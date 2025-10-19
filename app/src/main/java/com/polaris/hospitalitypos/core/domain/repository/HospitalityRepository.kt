package com.polaris.hospitalitypos.core.domain.repository

import com.polaris.hospitalitypos.core.domain.model.DailyDigest
import com.polaris.hospitalitypos.core.domain.model.Payment
import com.polaris.hospitalitypos.core.domain.model.Property
import com.polaris.hospitalitypos.core.domain.model.Reservation
import com.polaris.hospitalitypos.core.domain.model.Room
import com.polaris.hospitalitypos.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

interface HospitalityRepository {
    fun observeProperties(): Flow<List<Property>>
    fun observeRooms(propertyId: UUID): Flow<List<Room>>
    fun observeReservations(propertyId: UUID): Flow<List<Reservation>>
    suspend fun getReservationsForRoom(roomId: UUID, startDate: LocalDate, endDate: LocalDate): List<Reservation>
    suspend fun saveReservation(reservation: Reservation)
    suspend fun savePayment(payment: Payment)
    fun observePaymentsForReservation(reservationId: UUID): Flow<List<Payment>>
    suspend fun fetchRemoteInsights(): Result<String>
    suspend fun syncPendingJobs(): Result<Unit>
    suspend fun getDailyDigest(propertyId: UUID): DailyDigest
    suspend fun currentUser(): User
}
