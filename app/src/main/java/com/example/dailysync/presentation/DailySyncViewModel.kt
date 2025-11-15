package com.example.dailysync.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.data.InMemoryDailyReportRepository
import com.example.dailysync.domain.DailyReport
import com.example.dailysync.domain.usecase.CreateDailyReportUseCase
import com.example.dailysync.domain.usecase.ObserveDailyReportsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * DailySync 画面の ViewModel。
 *
 * - 入力状態
 * - 日報一覧の監視
 * - 保存ボタン押下時の処理
 * を担当する。
 */
class DailySyncViewModel(
    // 本来は DI で渡すが、サンプルなのでここで組み立て
    repository: InMemoryDailyReportRepository = InMemoryDailyReportRepository()
) : ViewModel() {

    private val createDailyReportUseCase = CreateDailyReportUseCase(repository)
    private val observeDailyReportsUseCase = ObserveDailyReportsUseCase(repository)

    // 入力中の日報
    var inputDate by mutableStateOf(LocalDate.now())
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
            } catch (e: Exception) {
                errorMessage = "保存に失敗しました"
            }
        }
    }
}
