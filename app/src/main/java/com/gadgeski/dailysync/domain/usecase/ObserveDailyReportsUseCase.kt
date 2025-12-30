package com.gadgeski.dailysync.domain.usecase

import com.gadgeski.dailysync.data.DailyReportRepository
import com.gadgeski.dailysync.domain.DailyReport
import kotlinx.coroutines.flow.Flow

/**
 * 日報一覧を監視するユースケース。
 */
class ObserveDailyReportsUseCase(
    private val repository: DailyReportRepository,
) {

    operator fun invoke(): Flow<List<DailyReport>> = repository.observeAll()
}
