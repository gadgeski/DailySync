// domain/usecase/ScheduleDailyReminderUseCase.kt
package com.example.dailysync.domain.usecase

import com.example.dailysync.domain.reminder.ReminderScheduler
import java.time.LocalTime

/**
 * 日次リマインダーのON/OFFを切り替えるユースケース。
 *
 * - enable(time): 指定した時刻に毎日リマインダーを予約する
 * - disable(): 現在のリマインダーをキャンセルする
 *
 * いずれ設定画面や「リマインダーON/OFF」のUIから呼び出す想定。
 */
@Suppress("unused") // ★ 現時点では呼び出し側が未実装なため、IDEの未使用警告を一旦抑制
class ScheduleDailyReminderUseCase(
    private val scheduler: ReminderScheduler,
) {

    /**
     * 指定時刻に毎日リマインダーを有効化する。
     *
     * @param time 毎日通知したいローカル時刻
     */
    fun enable(time: LocalTime) {
        scheduler.scheduleDailyReminder(time)
    }

    /**
     * 現在設定されている日次リマインダーを無効化する。
     */
    fun disable() {
        scheduler.cancelDailyReminder()
    }
}
