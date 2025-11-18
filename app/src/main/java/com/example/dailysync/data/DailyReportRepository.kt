// data/DailyReportRepository.kt
package com.example.dailysync.data

import com.example.dailysync.domain.DailyReport
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * 日報の保存・取得を行うリポジトリのインターフェース
 */
interface DailyReportRepository {

    /**
     * 日報を保存（新規または更新）
     */
    suspend fun save(report: DailyReport): DailyReport

    /**
     * 指定した日付の日報を1件取得（なければ null）
     */
    suspend fun findByDate(date: LocalDate): DailyReport?

    /**
     * 全ての日報を新しい順で監視する
     */
    fun observeAll(): Flow<List<DailyReport>>
}
