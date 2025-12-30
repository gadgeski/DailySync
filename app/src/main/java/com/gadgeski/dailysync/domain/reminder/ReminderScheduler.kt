package com.gadgeski.dailysync.domain.reminder

import java.time.LocalTime

interface ReminderScheduler {
    fun scheduleDailyReminder(time: LocalTime)
    fun cancelDailyReminder()
}
