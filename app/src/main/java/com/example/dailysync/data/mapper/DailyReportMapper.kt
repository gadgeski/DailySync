// data/mapper/DailyReportMapper.kt
package com.example.dailysync.data.mapper

import com.example.dailysync.data.local.DailyReportEntity
import com.example.dailysync.domain.DailyReport

fun DailyReportEntity.toDomain(): DailyReport =
    DailyReport(
        id = id,
        date = date,
        title = title,
        body = body,
        projectName = projectName,
        tags = tags,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun DailyReport.toEntity(): DailyReportEntity =
    DailyReportEntity(
        id = id,
        date = date,
        title = title,
        body = body,
        projectName = projectName,
        tags = tags,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
