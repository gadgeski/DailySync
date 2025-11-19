// data/reminder/AndroidReminderScheduler.kt
package com.example.dailysync.data.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.dailysync.domain.reminder.ReminderScheduler
import java.time.LocalTime
import java.util.Calendar

class AndroidReminderScheduler(
    private val context: Context,
    private val alarmManager: AlarmManager
) : ReminderScheduler {

    override fun scheduleDailyReminder(time: LocalTime) {
        val calendar = Calendar.getInstance().apply {
            val now = System.currentTimeMillis()
            timeInMillis = now

            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // もし指定時刻が「今より前」なら翌日にずらす
            if (timeInMillis <= now) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val pendingIntent = createPendingIntent()

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun cancelDailyReminder() {
        // 同じ PendingIntent を渡すと、その予約だけキャンセルされる
        val pendingIntent = createPendingIntent()
        alarmManager.cancel(pendingIntent)
    }

    /**
     * リマインダー用の PendingIntent を作成する共通メソッド。
     * schedule / cancel の両方で同じものを使う。
     */
    private fun createPendingIntent(): PendingIntent {
        // ★ ここで実際に Intent を new して初期化する（コメントではなくコード！）
        val intent = Intent(context, DailySyncReminderReceiver::class.java).apply {
            action = ACTION_DAILY_SYNC_REMINDER
        }

        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        private const val REQUEST_CODE = 1001
        const val ACTION_DAILY_SYNC_REMINDER =
            "com.example.dailysync.ACTION_DAILY_SYNC_REMINDER"
    }
}