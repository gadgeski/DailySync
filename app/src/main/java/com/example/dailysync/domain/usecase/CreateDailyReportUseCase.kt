package com.example.dailysync.domain.usecase

import com.example.dailysync.data.DailyReportRepository
import com.example.dailysync.domain.DailyReport
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 「日報を作成する」ユースケース。
 * - 同じ日付の日報が既にあれば上書き
 * - バリデーションもここでまとめて行う
 */
class CreateDailyReportUseCase(
    private val repository: DailyReportRepository,
) {

    suspend operator fun invoke(
        date: LocalDate,
        title: String,
        body: String,
    ): DailyReport {
        require(title.isNotBlank()) { "タイトルは必須です" }

        val now = LocalDateTime.now()
        val existing = repository.findByDate(date)

        // 簡単な構成式ではElvisがオススメ
        // 複雑な分岐がある場合はifがオススメ

        val report = existing
            ?.copy(
                title = title,
                body = body,
                updatedAt = now,
            )
            ?: DailyReport(
                id = 0L,
                date = date,
                title = title,
                body = body,
                createdAt = now,
                updatedAt = now,
            )

        return repository.save(report)
    }
}
