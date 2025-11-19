// presentation/DailySyncScreen.kt
@file:OptIn(ExperimentalMaterial3Api::class)
// ★ このファイル全体で ExperimentalMaterial3Api を許可

package com.example.dailysync.presentation

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.dailysync.domain.DailyReport
import com.example.dailysync.domain.export.ExportFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

// ★ 変更: androidx.hilt.navigation.compose.hiltViewModel → androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
// ★ 追加: 共有インテント用(android.content.Intent)
// ★ 追加: suspend 関数呼び出し用(androidx.compose.runtime.rememberCoroutineScope)
// ★ 追加: 共有インテント発行用にContextが必要(androidx.compose.ui.platform.LocalContext)
// ★ 追加: Hilt版 ViewModel 取得(androidx.hilt.navigation.compose.hiltViewModel)
// ★ 追加: エクスポート形式(enum)(com.example.dailysync.domain.export.ExportFormat)
// ★ 追加: coroutineScope.launch で使用(kotlinx.coroutines.launch)
// ★ 追加: (androidx.compose.material3.ExperimentalMaterial3Api)
// ★ 追加: DatePicker用の状態保持に必要(androidx.compose.runtime.remember)
// ★ 追加: DatePicker表示フラグ用(androidx.compose.runtime.mutableStateOf)
// ★ 追加: Material3 DatePicker(androidx.compose.material3.DatePicker)
// ★ 追加: Material3 DatePickerDialog(androidx.compose.material3.DatePickerDialog)
// ★ 追加: DatePickerの状態(androidx.compose.material3.rememberDatePickerState)
// ★ 追加: DatePickerの選択値をLocalDateに変換するため(java.time.Instant)
// ★ 追加: タイムゾーン取得(java.time.ZoneId)
// ★ 追加: LocalDate型(java.time.LocalDate)

@Composable
fun DailySyncScreen(
    viewModel: DailySyncViewModel = hiltViewModel()
    // ★ 変更: Hilt から ViewModel を取得
) {
    val reports by viewModel.reports.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DailySync 日報") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            DailySyncInputSection(viewModel)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "過去の日報",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            DailySyncReportList(reports)
        }
    }
}

@Composable
private fun DailySyncInputSection(
    viewModel: DailySyncViewModel
) {
    val errorMessage = viewModel.errorMessage

    // ★ 日付ピッカー表示フラグ
    val showDatePicker = remember { mutableStateOf(false) }
    // ★ 日付表示用フォーマッタ
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }

    // ★ エクスポート処理を行うための CoroutineScope & Context
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column {
        // ★ 日付選択ボタン（現在の日付を表示）
        TextButton(
            onClick = { showDatePicker.value = true }
        ) {
            Text(text = "日付: ${viewModel.inputDate.format(dateFormatter)}")
        }

        // ★ DatePickerDialog
        if (showDatePicker.value) {
            val datePickerState = rememberDatePickerState(
                // ★ 現在の入力日付を初期選択にする
                initialSelectedDateMillis = viewModel.inputDate
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val millis = datePickerState.selectedDateMillis
                            if (millis != null) {
                                val localDate: LocalDate = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                viewModel.onDateChange(localDate)
                            }
                            showDatePicker.value = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker.value = false }
                    ) {
                        Text("キャンセル")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }

        OutlinedTextField(
            value = viewModel.inputTitle,
            onValueChange = viewModel::onTitleChange,
            label = { Text("タイトル") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = viewModel.inputBody,
            onValueChange = viewModel::onBodyChange,
            label = { Text("内容") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 保存ボタン
        Button(
            onClick = { viewModel.onSaveClick() }
        ) {
            Text("保存")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ★ 追加: Markdown 形式で日報をエクスポートして共有するボタン
        Button(
            onClick = {
                scope.launch {
                    try {
                        // ViewModel 経由でエクスポート処理を実行
                        val markdown = viewModel.exportReports(ExportFormat.MARKDOWN)

                        // ★ 空文字の場合は共有をスキップ（例外ではないが無意味なので防御）
                        if (markdown.isBlank()) {
                            return@launch
                        }

                        // 共有インテントを作成して、他アプリ（メモ / メール / LINE など）に渡す
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, markdown)
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "日報を共有")
                        context.startActivity(shareIntent)
                    } catch (_: Exception) {
                        // ★ 簡易対策: 例外が出ても画面クラッシュは防ぐ
                        //   必要なら Snackbar や Toast でエラー表示を付けても良い
                    }
                }
            }
        ) {
            Text("Markdownでエクスポート")
        }
    }
}

@Composable
private fun DailySyncReportList(
    reports: List<DailyReport>
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    LazyColumn {
        items(reports) { report ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "${report.date.format(dateFormatter)}  ${report.title}",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = report.body,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
