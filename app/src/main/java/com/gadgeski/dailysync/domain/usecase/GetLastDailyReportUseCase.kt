package com.gadgeski.dailysync.domain.usecase

import com.gadgeski.dailysync.data.DailyReportRepository
import com.gadgeski.dailysync.domain.DailyReport
import javax.inject.Inject

class GetLastDailyReportUseCase @Inject constructor(
    private val repository: DailyReportRepository,
) {
    suspend operator fun invoke(): DailyReport? = repository.findLast()
}
