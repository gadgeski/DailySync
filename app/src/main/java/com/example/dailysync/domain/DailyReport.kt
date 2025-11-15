package com.example.dailysync.domain

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 日報を表現するドメインモデル
 */
data class DailyReport(
    val id: Long,                    // 永続化用ID
    val date: LocalDate,             // 日付
    val title: String,               // タイトル（簡単な要約）
    val body: String,                // 詳細な内容
    val createdAt: LocalDateTime,    // 作成日時
    val updatedAt: LocalDateTime     // 更新日時
)
