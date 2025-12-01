// presentation/DailySyncViewModel.kt
package com.example.dailysync.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.data.UserPreferences
import com.example.dailysync.domain.DailyReport
import com.example.dailysync.domain.export.ExportFormat
import com.example.dailysync.domain.usecase.CreateDailyReportUseCase
import com.example.dailysync.domain.usecase.ExportDailyReportsUseCase
import com.example.dailysync.domain.usecase.GetLastDailyReportUseCase
import com.example.dailysync.domain.usecase.ObserveDailyReportsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

// ★ 追加: エクスポート形式(enum)を使用(com.example.dailysync.domain.export.ExportFormat)
// ★ 追加: エクスポート用UseCaseを依存として受け取る(com.example.dailysync.domain.usecase.ExportDailyReportsUseCase)

/**
 * DailySync 画面の ViewModel。
 *
 * - 入力状態
 * - 日報一覧の監視
 * - 保存ボタン押下時の処理
 * - 日報エクスポート処理の仲介
 * を担当する。
 */
@HiltViewModel
class DailySyncViewModel @Inject constructor(
    private val createDailyReportUseCase: CreateDailyReportUseCase,
    // ★ 修正: プロパティにせず、単なるコンストラクタ引数として受け取る
    observeDailyReportsUseCase: ObserveDailyReportsUseCase,
    // ★ 追加: エクスポート用ユースケースを依存として注入
    private val exportDailyReportsUseCase: ExportDailyReportsUseCase,
    private val getLastDailyReportUseCase: GetLastDailyReportUseCase,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    // 入力中の日報
    var inputDate: LocalDate by mutableStateOf(LocalDate.now())
        private set

    var inputTitle by mutableStateOf("")
        private set

    var inputBody by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // New States
    var lastReport by mutableStateOf<DailyReport?>(null)
        private set

    var isPreviewMode by mutableStateOf(false)
        private set

    var issueTrackerUrlBase by mutableStateOf("")
        private set

    // 一覧表示用
    val reports: StateFlow<List<DailyReport>> =
        observeDailyReportsUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
            )

    init {
        loadLastReport()
        viewModelScope.launch {
            userPreferences.issueTrackerUrlBase.collect { url ->
                issueTrackerUrlBase = url
            }
        }
    }

    private fun loadLastReport() {
        viewModelScope.launch {
            lastReport = getLastDailyReportUseCase()
        }
    }

    fun onTitleChange(newTitle: String) {
        inputTitle = newTitle
    }

    fun onBodyChange(newBody: String) {
        inputBody = newBody
    }

    fun onDateChange(newDate: LocalDate) {
        inputDate = newDate
    }

    fun togglePreviewMode() {
        isPreviewMode = !isPreviewMode
    }

    fun updateIssueTrackerUrlBase(url: String) {
        viewModelScope.launch {
            userPreferences.setIssueTrackerUrlBase(url)
        }
    }

    fun smartCopyLastReport() {
        val report = lastReport ?: return

        // Simple Smart Copy Logic: Replace dates in the body with today's date
        // This is a heuristic. We assume dates are in format YYYY-MM-DD or MM/DD
        val today = LocalDate.now()
        val yesterday = report.date

        // Formats to look for
        val formats = listOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MM/dd"),
            DateTimeFormatter.ofPattern("MM.dd"),
        )

        var newBody = report.body

        // A more robust way would be to find dates that match 'yesterday' and replace with 'today'
        // But the requirement says "update date notation to today's date".
        // Let's try to replace the string representation of yesterday with today.

        formats.forEach { formatter ->
            val yesterdayStr = yesterday.format(formatter)
            val todayStr = today.format(formatter)
            newBody = newBody.replace(yesterdayStr, todayStr)
        }

        inputTitle = report.title // Copy title too? Maybe useful.
        inputBody = newBody
    }

    fun onSaveClick() {
        viewModelScope.launch {
            try {
                createDailyReportUseCase(
                    date = inputDate,
                    title = inputTitle,
                    body = inputBody,
                )
                errorMessage = null
                // Reload last report as the one we just saved might become the last one
                loadLastReport()
            } catch (e: IllegalArgumentException) {
                errorMessage = e.message
            } catch (_: Exception) {
                errorMessage = "保存に失敗しました"
            }
        }
    }

    suspend fun exportReports(format: ExportFormat): String = exportDailyReportsUseCase(format)
}
