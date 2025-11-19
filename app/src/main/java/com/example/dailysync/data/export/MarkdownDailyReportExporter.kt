// data/export/MarkdownDailyReportExporter.kt
package com.example.dailysync.data.export

import com.example.dailysync.domain.DailyReport
import com.example.dailysync.domain.export.DailyReportExporter
import com.example.dailysync.domain.export.ExportFormat
import java.time.format.DateTimeFormatter
import kotlin.text.buildString

// ★ 削除: 不要 & エラーの原因になりうる(kotlin.text.appendLine)

/**
 * 日報一覧を Text / Markdown 形式の文字列に変換するエクスポータ。
 */
class MarkdownDailyReportExporter : DailyReportExporter {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun export(
        reports: List<DailyReport>,
        format: ExportFormat
    ): String {
        return when (format) {
            ExportFormat.TEXT -> exportAsText(reports)
            ExportFormat.MARKDOWN -> exportAsMarkdown(reports)
        }
    }

    // Text形式
    private fun exportAsText(reports: List<DailyReport>): String =
        buildString {
            for (report in reports) {
                appendLine("=== ${report.date.format(dateFormatter)} ===")
                appendLine("[${report.title}]")
                appendLine(report.body)
                appendLine()
            // 空行 ★ コメントを同じ行に寄せて読みやすく
            }
        }

    // Markdown形式
    private fun exportAsMarkdown(reports: List<DailyReport>): String =
        buildString {
            for (report in reports) {
                appendLine("## ${report.date.format(dateFormatter)}  ${report.title}")

                if (!report.projectName.isNullOrBlank()) {
                    appendLine("- Project: ${report.projectName}")
                }

                if (report.tags.isNotEmpty()) {
                    // ★ ダブルクォートのエスケープも不要なので素直な書き方に変更
                    appendLine("- Tags: ${report.tags.joinToString(", ")}")
                }

                // ★ ここはifの外に出す前提なら、インデントをforブロックと揃える
                appendLine()
                appendLine(report.body)
                appendLine()
            }
        }
}
