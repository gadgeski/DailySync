// data/local/DailyReportDao.kt
package com.gadgeski.dailysync.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DailyReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(report: DailyReportEntity): Long

    @Query("SELECT * FROM daily_reports WHERE date = :date LIMIT 1")
    suspend fun findByDate(date: LocalDate): DailyReportEntity?

    @Query("SELECT * FROM daily_reports ORDER BY date DESC LIMIT 1")
    suspend fun findLast(): DailyReportEntity?

    @Query("SELECT * FROM daily_reports ORDER BY date DESC")
    fun observeAll(): Flow<List<DailyReportEntity>>

    // ★ observeFiltered は一旦削除 or 後回し
}
