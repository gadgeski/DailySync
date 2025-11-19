// presentation/DailySyncViewModel.kt
package com.example.dailysync.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.domain.DailyReport
import com.example.dailysync.domain.export.ExportFormat
import com.example.dailysync.domain.usecase.CreateDailyReportUseCase
import com.example.dailysync.domain.usecase.ExportDailyReportsUseCase
import com.example.dailysync.domain.usecase.ObserveDailyReportsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
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
    private val exportDailyReportsUseCase: ExportDailyReportsUseCase
) : ViewModel() {

    // 入力中の日報
    // ★ 修正: 型を明示してプラットフォーム型警告を解消
    var inputDate: LocalDate by mutableStateOf(LocalDate.now())
        private set

    var inputTitle by mutableStateOf("")
        private set

    var inputBody by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // 一覧表示用
    val reports: StateFlow<List<DailyReport>> =
        observeDailyReportsUseCase()
            // ★ 変更: インジェクトされた UseCase（引数）をそのまま呼び出し
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

    fun onTitleChange(newTitle: String) {
        inputTitle = newTitle
    }

    fun onBodyChange(newBody: String) {
        inputBody = newBody
    }

    fun onDateChange(newDate: LocalDate) {
        inputDate = newDate
    }

    fun onSaveClick() {
        viewModelScope.launch {
            try {
                createDailyReportUseCase(
                    date = inputDate,
                    title = inputTitle,
                    body = inputBody
                )
                // 成功時に必要なら入力をクリアするなど
                // inputTitle = ""
                // inputBody = ""
                errorMessage = null
            } catch (e: IllegalArgumentException) {
                errorMessage = e.message
            } catch (_: Exception) { // ★ 修正: 使わない例外パラメータは _ にして意図を明示
                errorMessage = "保存に失敗しました"
            }
        }
    }

    // ★ 追加: 画面から日報をエクスポートするためのAPI
    // Compose 側では rememberCoroutineScope.launch { viewModel.exportReports(…) } のように呼ぶ想定
    suspend fun exportReports(format: ExportFormat): String {
        return exportDailyReportsUseCase(format)
    }
}
