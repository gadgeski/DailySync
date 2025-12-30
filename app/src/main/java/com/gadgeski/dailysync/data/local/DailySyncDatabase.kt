// data/local/DailySyncDatabase.kt
package com.gadgeski.dailysync.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [DailyReportEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class DailySyncDatabase : RoomDatabase() {
    abstract fun dailyReportDao(): DailyReportDao
}
