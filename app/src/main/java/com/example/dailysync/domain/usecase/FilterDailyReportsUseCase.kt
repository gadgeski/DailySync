// domain/usecase/FilterDailyReportsUseCase.kt
package com.example.dailysync.domain.usecase

import com.example.dailysync.domain.DailyReport

/**
 * 日報一覧に対してメモリ上でフィルタをかけるための条件。
 */
data class ReportFilter(
    val projectName: String? = null,
    val tag: String? = null,
)

/**
 * メモリ上の一覧に対してフィルタするユースケース。
 *
 * - 今は ViewModel などからまだ呼んでいないので IDE からは "never used" 警告が出る。
 * - 将来的に「プロジェクト名／タグで絞り込み」機能を実装するときに利用する前提の設計パーツ。
 */
@Suppress("unused") // ★ 現時点では利用箇所がないための警告を一旦抑制
class FilterDailyReportsUseCase {

    /**
     * @param reports フィルタ対象の日報一覧
     * @param filter  null の場合はフィルタせず、そのまま返す
     */
    operator fun invoke(
        reports: List<DailyReport>,
        filter: ReportFilter?,
    ): List<DailyReport> {
        // ★ フィルタ条件が null の場合は、そのまま返す（「フィルタなし」として扱う）
        if (filter == null) return reports

        return reports.filter { report ->
            val projectOk = filter.projectName
                ?.let { projectName -> report.projectName == projectName }
                ?: true

            val tagOk = filter.tag
                ?.let { tag -> report.tags.contains(tag) }
                ?: true

            projectOk && tagOk
        }
    }
}
