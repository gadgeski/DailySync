package com.example.dailysync.domain.usecase

import com.example.dailysync.data.DailyReportRepository
import com.example.dailysync.domain.DailyReport
import kotlinx.coroutines.flow.Flow

/**
 * 日報一覧を監視するユースケース。
 */
class ObserveDailyReportsUseCase(
    private val repository: DailyReportRepository
) {

    operator fun invoke(): Flow<List<DailyReport>> {
        return repository.observeAll()
    }
}
