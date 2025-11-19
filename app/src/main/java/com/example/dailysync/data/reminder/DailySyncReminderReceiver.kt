// domain/reminder/ReminderScheduler.kt
package com.example.dailysync.data.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DailySyncReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != AndroidReminderScheduler.ACTION_DAILY_SYNC_REMINDER) {
            return
        }

        // TODO: 本当はここで Notification を出す
        Toast.makeText(context, "DailySync 日報リマインダー", Toast.LENGTH_SHORT).show()
    }
}
