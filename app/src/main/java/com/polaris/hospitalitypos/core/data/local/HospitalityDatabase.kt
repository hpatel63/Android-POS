package com.polaris.hospitalitypos.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.polaris.hospitalitypos.core.data.local.converter.OffsetDateTimeConverter
import com.polaris.hospitalitypos.core.data.local.converter.LocalDateConverter

@Database(
    entities = [
        PropertyEntity::class,
        RoomEntity::class,
        GuestEntity::class,
        ReservationEntity::class,
        PaymentEntity::class,
        TaxProfileEntity::class,
        UserEntity::class,
        TemplateEntity::class,
        GuestIncidentEntity::class,
        AuditLogEntity::class,
        SyncJobEntity::class,
        ManualArticleEntity::class,
        AiPromptLogEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(OffsetDateTimeConverter::class, LocalDateConverter::class)
abstract class HospitalityDatabase : RoomDatabase() {
    abstract fun hospitalityDao(): HospitalityDao
}
