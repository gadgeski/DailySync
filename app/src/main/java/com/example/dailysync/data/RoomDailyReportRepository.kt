// data/RoomDailyReportRepository.kt
package com.example.dailysync.data

import com.example.dailysync.data.local.DailyReportDao
import com.example.dailysync.data.mapper.toDomain
import com.example.dailysync.data.mapper.toEntity
import com.example.dailysync.domain.DailyReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class RoomDailyReportRepository(
    private val dao: DailyReportDao,
) : DailyReportRepository {

    override suspend fun save(report: DailyReport): DailyReport {
        val id = dao.upsert(report.toEntity())
        return report.copy(id = if (report.id == 0L) id else report.id)
    }

    override suspend fun findByDate(date: LocalDate): DailyReport? = dao.findByDate(date)?.toDomain()

    override suspend fun findLast(): DailyReport? = dao.findLast()?.toDomain()

    override fun observeAll(): Flow<List<DailyReport>> = dao.observeAll().map { list ->
        list.map { it.toDomain() }
    }
}
