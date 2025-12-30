// app/src/main/java/com/example/dailysync/di/AppModule.kt
package com.gadgeski.dailysync.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.gadgeski.dailysync.data.DailyReportRepository
import com.gadgeski.dailysync.data.RoomDailyReportRepository
import com.gadgeski.dailysync.data.export.MarkdownDailyReportExporter
import com.gadgeski.dailysync.data.local.Converters
import com.gadgeski.dailysync.data.local.DailyReportDao
import com.gadgeski.dailysync.data.local.DailySyncDatabase
import com.gadgeski.dailysync.domain.export.DailyReportExporter
import com.gadgeski.dailysync.domain.usecase.CreateDailyReportUseCase
import com.gadgeski.dailysync.domain.usecase.ExportDailyReportsUseCase
import com.gadgeski.dailysync.domain.usecase.ObserveDailyReportsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// ★ Added: DataStore の定義をここに移動（UserPreferences.kt から移動）
// 設定ファイル名は "settings" で統一
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ★ Added: DataStore<Preferences> を提供するメソッド
    // これにより、UserPreferences などのクラスで直接 DataStore を受け取れるようになります。
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun provideConverters(): Converters = Converters()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        converters: Converters,
    ): DailySyncDatabase = Room.databaseBuilder(
        context,
        DailySyncDatabase::class.java,
        "daily_sync.db",
    )
        .addTypeConverter(converters)
        .build()

    @Provides
    fun provideDailyReportDao(
        db: DailySyncDatabase,
    ): DailyReportDao = db.dailyReportDao()

    @Provides
    @Singleton
    fun provideDailyReportRepository(
        dao: DailyReportDao,
    ): DailyReportRepository = RoomDailyReportRepository(dao)

    @Provides
    fun provideCreateDailyReportUseCase(
        repository: DailyReportRepository,
    ): CreateDailyReportUseCase = CreateDailyReportUseCase(repository)

    @Provides
    fun provideObserveDailyReportsUseCase(
        repository: DailyReportRepository,
    ): ObserveDailyReportsUseCase = ObserveDailyReportsUseCase(repository)

    @Provides
    @Singleton
    fun provideDailyReportExporter(): DailyReportExporter = MarkdownDailyReportExporter()

    @Provides
    @Singleton
    fun provideExportDailyReportsUseCase(
        repository: DailyReportRepository,
        exporter: DailyReportExporter,
    ): ExportDailyReportsUseCase = ExportDailyReportsUseCase(
        repository = repository,
        exporter = exporter,
    )
}
