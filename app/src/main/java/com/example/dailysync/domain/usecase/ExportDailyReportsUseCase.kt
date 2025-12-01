// domain/usecase/ExportDailyReportsUseCase.kt
package com.example.dailysync.domain.usecase

import com.example.dailysync.data.DailyReportRepository
import com.example.dailysync.domain.DailyReport
import com.example.dailysync.domain.export.DailyReportExporter
import com.example.dailysync.domain.export.ExportFormat
import kotlinx.coroutines.flow.firstOrNull

// ★ 追加: Flow の firstOrNull を使うための import(kotlinx.coroutines.flow.firstOrNull)
// ★ 追加: ドメインモデルをimport(com.example.dailysync.domain.DailyReport)
// ★ 削除: kotlin.collections はデフォルトimportなので明示不要(kotlin.collections.orEmpty)

class ExportDailyReportsUseCase(
    private val repository: DailyReportRepository,
    private val exporter: DailyReportExporter,
) {
    suspend operator fun invoke(format: ExportFormat): String {
        // ★ 型を DailyReport で明示しておくと読みやすい & 変な推論エラーを防げる
        val allReports: List<DailyReport> =
            repository.observeAll()
                // Flow<List<DailyReport>> なので、最初に流れてきた一覧を1回だけ取得
                .firstOrNull()
                // ★ ここで List<DailyReport>? が返る
                .orEmpty()
        // ★ null の場合は空リストにする（List<DailyReport> に確定）

        return exporter.export(allReports, format)
    }
}
