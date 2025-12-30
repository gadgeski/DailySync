package com.gadgeski.dailysync.data

import com.gadgeski.dailysync.domain.DailyReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

/**
 * デモ用のインメモリ実装。
 * 実運用では Room 版などに差し替える想定。
 */
@Suppress("unused")
class InMemoryDailyReportRepository : DailyReportRepository {

    private val reports = MutableStateFlow<List<DailyReport>>(emptyList())
    private var nextId: Long = 1L

    override suspend fun save(report: DailyReport): DailyReport {
        val existing = reports.value.find { it.id == report.id }
        val newReport = if (existing == null) {
            // 新規
            report.copy(id = nextId++)
        } else {
            // 更新
            report
        }

        reports.value = reports.value
            .filterNot { it.id == newReport.id }
            .plus(newReport)
            .sortedByDescending { it.date }

        return newReport
    }

    override suspend fun findByDate(date: LocalDate): DailyReport? = reports.value.find { it.date == date }

    override suspend fun findLast(): DailyReport? = reports.value.maxByOrNull { it.date }
    override fun observeAll(): Flow<List<DailyReport>> = reports.map { list ->
        list.sortedByDescending { it.date }
    }
}
