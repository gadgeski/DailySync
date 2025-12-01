// data/local/DailyReportEntity.kt
package com.example.dailysync.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "daily_reports")
data class DailyReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val date: LocalDate,
    val title: String,
    val body: String,
    val projectName: String?,
    // プロジェクト名（フィルタ用）
    val tags: List<String>,
    // タグ（TypeConverter利用）
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
