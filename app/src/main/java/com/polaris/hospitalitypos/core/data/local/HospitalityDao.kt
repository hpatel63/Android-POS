package com.polaris.hospitalitypos.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

@Dao
interface HospitalityDao {
    @Query("SELECT * FROM properties WHERE is_deleted = 0")
    fun observeProperties(): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM rooms WHERE property_id = :propertyId AND is_deleted = 0")
    fun observeRooms(propertyId: UUID): Flow<List<RoomEntity>>

    @Query("SELECT * FROM reservations WHERE property_id = :propertyId")
    fun observeReservations(propertyId: UUID): Flow<List<ReservationEntity>>

    @Query("SELECT * FROM reservations WHERE room_id = :roomId AND ((check_in <= :endDate AND check_out >= :startDate))")
    suspend fun getReservationsForRoom(roomId: UUID, startDate: LocalDate, endDate: LocalDate): List<ReservationEntity>

    @Transaction
    @Query("SELECT * FROM reservations WHERE id = :reservationId")
    suspend fun getReservationWithRelations(reservationId: UUID): ReservationWithRelations?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProperties(properties: List<PropertyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRooms(rooms: List<RoomEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGuests(guests: List<GuestEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReservations(reservations: List<ReservationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPayments(payments: List<PaymentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun enqueueSyncJob(job: SyncJobEntity)

    @Query("SELECT * FROM sync_queue ORDER BY created_at LIMIT 50")
    suspend fun getPendingSyncJobs(): List<SyncJobEntity>

    @Query("DELETE FROM sync_queue WHERE id = :id")
    suspend fun deleteSyncJob(id: UUID)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertManualArticles(articles: List<ManualArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAiPromptLog(log: AiPromptLogEntity)

    @Query("SELECT * FROM manual_articles")
    fun observeManualArticles(): Flow<List<ManualArticleEntity>>

    @Query("SELECT * FROM payments WHERE reservation_id = :reservationId")
    fun observePaymentsForReservation(reservationId: UUID): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE reservation_id IN (SELECT id FROM reservations WHERE property_id = :propertyId)")
    fun observePaymentsForProperty(propertyId: UUID): Flow<List<PaymentEntity>>

    @Update
    suspend fun updateRoom(room: RoomEntity)
}
