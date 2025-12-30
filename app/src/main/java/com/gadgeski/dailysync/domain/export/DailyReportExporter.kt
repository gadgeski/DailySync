// domain/export/DailyReportExporter.kt
package com.gadgeski.dailysync.domain.export

import com.gadgeski.dailysync.domain.DailyReport

enum class ExportFormat {
    TEXT,
    MARKDOWN,
}

interface DailyReportExporter {
    fun export(reports: List<DailyReport>, format: ExportFormat): String
}
