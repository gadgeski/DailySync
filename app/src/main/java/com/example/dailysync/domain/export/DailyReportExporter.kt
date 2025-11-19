// domain/export/DailyReportExporter.kt
package com.example.dailysync.domain.export

import com.example.dailysync.domain.DailyReport

enum class ExportFormat {
    TEXT,
    MARKDOWN
}

interface DailyReportExporter {
    fun export(reports: List<DailyReport>, format: ExportFormat): String
}
