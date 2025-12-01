// presentation/DailySyncScreen.kt

@file:Suppress("ktlint:standard:function-naming")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dailysync.presentation

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.dailysync.domain.DailyReport
import com.example.dailysync.domain.export.ExportFormat
import com.example.dailysync.presentation.components.MarkdownText
import com.example.dailysync.ui.theme.LuxuryCopper
import com.example.dailysync.ui.theme.LuxuryDeepBrown
import com.example.dailysync.ui.theme.LuxuryGlassBorder
import com.example.dailysync.ui.theme.LuxuryGlassSurface
import com.example.dailysync.ui.theme.LuxuryGold
import com.example.dailysync.ui.theme.LuxuryTextPrimary
import com.example.dailysync.ui.theme.LuxuryTextSecondary
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/* ========================================================
   DailySync: "Sunset Lounge" Edition
   温かみのある高級感・エディトリアルデザイン実装版
   ======================================================== */

@Composable
fun DailySyncScreen(
    viewModel: DailySyncViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit,
) {
    val reports by viewModel.reports.collectAsState()
    val lastReport = viewModel.lastReport
    val isPreviewMode = viewModel.isPreviewMode
    val issueTrackerUrlBase = viewModel.issueTrackerUrlBase

    // ★ グラデーション刷新: 温かみのある夕暮れ色
    // 上(光) -> 下(闇) への自然な減光
    val backgroundBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                LuxuryCopper,
                // 上部: 柔らかい赤銅色
                LuxuryDeepBrown,
                // 下部: 深いブラウン
                Color.Black,
                // 最下部: 引き締め
            ),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush),
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                // Settings Button at top right
                Box(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(end = 16.dp, top = 8.dp)) {
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.align(Alignment.TopEnd),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = LuxuryTextPrimary,
                        )
                    }
                }
            },
            contentWindowInsets = WindowInsets.statusBars,
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    // ★ 少し余白を増やしてゆったりさせる
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(28.dp),
                // ★ 要素間隔も広めに
            ) {
                // ──── Header Section (Editorial Serif) ────
                val headerFormatter = remember { DateTimeFormatter.ofPattern("MM.dd") }
                val dateString = viewModel.inputDate.format(headerFormatter)

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = dateString,
                    // ★ Type.kt の displayLarge (Serif体) が適用される
                    style = MaterialTheme.typography.displayLarge,
                    color = LuxuryTextPrimary,
                    modifier = Modifier.fillMaxWidth(),
                )

                // ──── Reference Panel (Yesterday's Log) ────
                if (lastReport != null) {
                    ReferencePanel(
                        report = lastReport,
                        onSmartCopy = { viewModel.smartCopyLastReport() },
                    )
                }

                // ──── Glass Card Section (Input / Preview) ────
                GlassCard {
                    // Toggle Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = if (isPreviewMode) "PREVIEW MODE" else "EDIT MODE",
                            style = MaterialTheme.typography.labelMedium,
                            color = LuxuryGold,
                        )
                        IconButton(onClick = { viewModel.togglePreviewMode() }) {
                            Icon(
                                imageVector = if (isPreviewMode) Icons.Default.Edit else Icons.Default.Visibility,
                                contentDescription = "Toggle Preview",
                                tint = LuxuryGold,
                            )
                        }
                    }

                    AnimatedContent(
                        targetState = isPreviewMode,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                        },
                        label = "EditPreviewTransition",
                    ) { targetPreview ->
                        if (targetPreview) {
                            // Preview Mode
                            Column(modifier = Modifier.fillMaxWidth().heightIn(min = 300.dp)) {
                                Text(
                                    text = viewModel.inputTitle.ifBlank { "No Title" },
                                    style = MaterialTheme.typography.titleLarge,
                                    color = LuxuryTextPrimary,
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                MarkdownText(
                                    markdown = viewModel.inputBody.ifBlank { "No content..." },
                                    issueTrackerUrlBase = issueTrackerUrlBase,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        } else {
                            // Edit Mode
                            DailySyncInputSection(viewModel)
                        }
                    }
                }

                // ──── List Section ────
                Text(
                    text = "HISTORY / LOGS",
                    // ★ Type.kt の labelLarge (SansSerif/Wide) が適用される
                    style = MaterialTheme.typography.labelLarge,
                    color = LuxuryTextSecondary,
                )

                DailySyncReportList(reports)
            }
        }
    }
}

/* ─────────────────────────────────────────────────────────
   Helper Components
   ───────────────────────────────────────────────────────── */

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            // ★ 暖色系のガラス色
            containerColor = LuxuryGlassSurface,
        ),
        // ★ ゴールドの繊細な枠線
        border = BorderStroke(1.dp, LuxuryGlassBorder),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        // 中身のpaddingも少しゆったりと
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            content()
        }
    }
}

@Composable
private fun DailySyncInputSection(
    viewModel: DailySyncViewModel,
) {
    val errorMessage = viewModel.errorMessage
    val showDatePicker = remember { mutableStateOf(false) }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy.MM.dd") }
    // ドット区切りでおしゃれに
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // ★ Date Selection (Serif Font)
    TextButton(
        onClick = { showDatePicker.value = true },
    ) {
        Text(
            text = "Target Date: ${viewModel.inputDate.format(dateFormatter)}",
            style = MaterialTheme.typography.titleMedium,
            // Serif
            color = LuxuryGold,
            fontWeight = FontWeight.Bold,
        )
    }

    if (showDatePicker.value) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = viewModel.inputDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val localDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                        viewModel.onDateChange(localDate)
                    }
                    showDatePicker.value = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) { Text("CANCEL") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // ★ Inputs (Elegant)
    LuxuryTextField(
        value = viewModel.inputTitle,
        onValueChange = viewModel::onTitleChange,
        label = "Title",
    )

    LuxuryTextField(
        value = viewModel.inputBody,
        onValueChange = viewModel::onBodyChange,
        label = "Thoughts & Progress",
        // 文言も少し情緒的に
        minHeight = 140.dp,
    )

    if (errorMessage != null) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
        )
    }

    // ★ Buttons
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // COMMIT LOG (Gold Button)
        Button(
            onClick = { viewModel.onSaveClick() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LuxuryGold,
                contentColor = LuxuryDeepBrown,
            ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                "COMMIT LOG",
                style = MaterialTheme.typography.labelLarge,
                // Wide spacing
                fontWeight = FontWeight.Black,
            )
        }

        // EXPORT (Outlined Gold)
        OutlinedButton(
            onClick = {
                scope.launch {
                    try {
                        val markdown = viewModel.exportReports(ExportFormat.MARKDOWN)
                        if (markdown.isBlank()) return@launch
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, markdown)
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share Log")
                        context.startActivity(shareIntent)
                    } catch (_: Exception) { }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.5f)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = LuxuryGold,
            ),
        ) {
            Text("EXPORT MARKDOWN", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun LuxuryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    minHeight: androidx.compose.ui.unit.Dp = 60.dp,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                color = LuxuryTextSecondary,
                style = MaterialTheme.typography.bodySmall,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = LuxuryTextPrimary,
            unfocusedTextColor = LuxuryTextPrimary,
            cursorColor = LuxuryGold,
            focusedBorderColor = LuxuryGold,
            unfocusedBorderColor = LuxuryGlassBorder,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(12.dp),
        textStyle = MaterialTheme.typography.bodyLarge,
        // Sans-Serif for input
    )
}

@Composable
private fun DailySyncReportList(
    reports: List<DailyReport>,
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp),
    ) {
        items(reports) { report ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LuxuryGlassSurface.copy(alpha = 0.05f),
                ),
                border = BorderStroke(1.dp, LuxuryGlassBorder.copy(alpha = 0.2f)),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Text(
                        text = report.title,
                        style = MaterialTheme.typography.titleMedium,
                        // Serif Headline
                        color = LuxuryTextPrimary,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${report.date.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = LuxuryGold.copy(alpha = 0.8f),
                        fontStyle = FontStyle.Italic,
                        // 日付を斜体にしてエレガントに
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = report.body,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        // Sans body
                        color = LuxuryTextSecondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReferencePanel(
    report: DailyReport,
    onSmartCopy: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LuxuryGlassSurface.copy(alpha = 0.1f),
        ),
        border = BorderStroke(1.dp, LuxuryGlassBorder.copy(alpha = 0.3f)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "YESTERDAY'S LOG",
                        style = MaterialTheme.typography.labelSmall,
                        color = LuxuryGold,
                    )
                    Text(
                        text = report.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LuxuryTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand",
                        tint = LuxuryTextSecondary,
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = report.body,
                        style = MaterialTheme.typography.bodySmall,
                        color = LuxuryTextSecondary,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onSmartCopy,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LuxuryGlassSurface,
                            contentColor = LuxuryGold,
                        ),
                        border = BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.5f)),
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Smart Copy to Today")
                    }
                }
            }
        }
    }
}
