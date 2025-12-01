package com.example.dailysync.domain.usecase

import com.example.dailysync.data.DailyReportRepository
import com.example.dailysync.domain.DailyReport
import javax.inject.Inject

class GetLastDailyReportUseCase @Inject constructor(
    private val repository: DailyReportRepository,
) {
    suspend operator fun invoke(): DailyReport? = repository.findLast()
}
