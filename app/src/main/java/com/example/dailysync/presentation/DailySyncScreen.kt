package com.example.dailysync.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailysync.domain.DailyReport
import java.time.format.DateTimeFormatter

@Composable
fun DailySyncScreen(
    viewModel: DailySyncViewModel = viewModel()
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

    Column {
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

        Button(
            onClick = { viewModel.onSaveClick() }
        ) {
            Text("保存")
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
