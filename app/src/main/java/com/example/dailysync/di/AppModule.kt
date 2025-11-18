// di/AppModule.kt
package com.example.dailysync.di

import android.content.Context
import androidx.room.Room
import com.example.dailysync.data.DailyReportRepository
import com.example.dailysync.data.RoomDailyReportRepository
import com.example.dailysync.data.export.MarkdownDailyReportExporter
import com.example.dailysync.data.local.Converters
import com.example.dailysync.data.local.DailyReportDao
import com.example.dailysync.data.local.DailySyncDatabase
import com.example.dailysync.domain.export.DailyReportExporter
import com.example.dailysync.domain.usecase.CreateDailyReportUseCase
import com.example.dailysync.domain.usecase.ExportDailyReportsUseCase
import com.example.dailysync.domain.usecase.ObserveDailyReportsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// ★ Hilt からのみ参照されるため、IDEの「never used」警告を抑制
@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ★ Converters を Hilt から提供して、Room に渡せるようにする
    @Provides
    @Singleton
    fun provideConverters(): Converters = Converters()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        converters: Converters
        // ★ Hilt 提供の Converters を受け取る
    ): DailySyncDatabase {
        return Room.databaseBuilder(
            context,
            DailySyncDatabase::class.java,
            "daily_sync.db"
        )
            .addTypeConverter(converters)
            // ★ new ではなく、DI から貰ったインスタンスを渡す
            .build()
    }

    @Provides
    fun provideDailyReportDao(
        db: DailySyncDatabase
    ): DailyReportDao = db.dailyReportDao()

    @Provides
    @Singleton
    fun provideDailyReportRepository(
        dao: DailyReportDao
    ): DailyReportRepository = RoomDailyReportRepository(dao)

    @Provides
    fun provideCreateDailyReportUseCase(
        repository: DailyReportRepository
    ): CreateDailyReportUseCase = CreateDailyReportUseCase(repository)

    @Provides
    fun provideObserveDailyReportsUseCase(
        repository: DailyReportRepository
    ): ObserveDailyReportsUseCase = ObserveDailyReportsUseCase(repository)

    @Provides
    @Singleton
    // ★ stateless な Exporter なのでシングルトンで十分
    // ★ ポイント: Export の実装を差し替えたい場合は「この関数の戻り値だけ変えればよい」
    //    例) PlainTextExporter や JsonExporter を作ってここで返す実装を切り替える
    fun provideDailyReportExporter(): DailyReportExporter {
        return MarkdownDailyReportExporter()
        // ★ 差し替えイメージ:
        // return PlainTextDailyReportExporter()
        // のように、この1行を変えるだけでアプリ全体のエクスポート形式を切り替えられる
    }

    // ★ エクスポート用ユースケースをDIグラフに載せる
    @Provides
    @Singleton
    // ★ UseCase も基本ステートレスなので Singleton で問題なし
    fun provideExportDailyReportsUseCase(
        repository: DailyReportRepository,
        exporter: DailyReportExporter
    ): ExportDailyReportsUseCase =
        ExportDailyReportsUseCase(
            repository = repository,
            exporter = exporter
        )
}
