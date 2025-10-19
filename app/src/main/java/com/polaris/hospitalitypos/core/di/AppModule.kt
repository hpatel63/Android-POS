package com.polaris.hospitalitypos.core.di

import android.content.Context
import androidx.room.Room
import com.polaris.hospitalitypos.core.ai.AiLogger
import com.polaris.hospitalitypos.core.ai.ChatGptClient
import com.polaris.hospitalitypos.core.ai.SecureConfigStore
import com.polaris.hospitalitypos.core.data.HospitalityRepositoryImpl
import com.polaris.hospitalitypos.core.data.local.HospitalityDao
import com.polaris.hospitalitypos.core.data.local.HospitalityDatabase
import com.polaris.hospitalitypos.core.data.local.SeedDataSeeder
import com.polaris.hospitalitypos.core.data.remote.HospitalityApi
import com.polaris.hospitalitypos.core.domain.repository.HospitalityRepository
import com.polaris.hospitalitypos.core.security.PiiRedactor
import com.polaris.hospitalitypos.core.utils.DateTimeProvider
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Singleton
import androidx.work.WorkManager

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        dateTimeProvider: DateTimeProvider
    ): HospitalityDatabase {
        val db = Room.databaseBuilder(context, HospitalityDatabase::class.java, "hospitality.db")
            .fallbackToDestructiveMigration()
            .build()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            SeedDataSeeder(db.hospitalityDao(), dateTimeProvider).seed()
        }
        return db
    }

    @Provides
    fun provideDao(db: HospitalityDatabase): HospitalityDao = db.hospitalityDao()

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .callTimeout(Duration.ofSeconds(30))
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.demo-hospitality.com")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): HospitalityApi = retrofit.create(HospitalityApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(
        dao: HospitalityDao,
        api: HospitalityApi,
        chatGptClient: ChatGptClient,
        redactor: PiiRedactor,
        dateTimeProvider: DateTimeProvider
    ): HospitalityRepository = HospitalityRepositoryImpl(dao, api, chatGptClient, redactor, dateTimeProvider)

    @Provides
    @Singleton
    fun provideSecureStore(@ApplicationContext context: Context): SecureConfigStore = SecureConfigStore(context)

    @Provides
    @Singleton
    fun provideAiLogger(dao: HospitalityDao, dateTimeProvider: DateTimeProvider): AiLogger = AiLogger(dao, dateTimeProvider)

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager = WorkManager.getInstance(context)
}
