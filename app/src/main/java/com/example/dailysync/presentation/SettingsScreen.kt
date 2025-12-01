package com.example.dailysync.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dailysync.ui.theme.LuxuryCopper
import com.example.dailysync.ui.theme.LuxuryDeepBrown
import com.example.dailysync.ui.theme.LuxuryGlassBorder
import com.example.dailysync.ui.theme.LuxuryGold
import com.example.dailysync.ui.theme.LuxuryTextPrimary
import com.example.dailysync.ui.theme.LuxuryTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentUrl: String,
    onUrlChange: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    var urlInput by remember(currentUrl) { mutableStateOf(currentUrl) }

    val backgroundBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                LuxuryCopper,
                LuxuryDeepBrown,
                Color.Black,
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
            contentWindowInsets = WindowInsets.statusBars,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Settings",
                            style = MaterialTheme.typography.titleLarge,
                            color = LuxuryTextPrimary,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = LuxuryTextPrimary,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                )
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Text(
                    text = "Issue Tracker Integration",
                    style = MaterialTheme.typography.titleMedium,
                    color = LuxuryGold,
                )

                Text(
                    text = "Enter the base URL for your issue tracker. Issue numbers (e.g. #123) will be automatically linked.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LuxuryTextSecondary,
                )

                OutlinedTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    label = { Text("Issue Tracker URL Base", color = LuxuryTextSecondary) },
                    placeholder = { Text("https://github.com/user/repo/issues/", color = LuxuryTextSecondary.copy(alpha = 0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
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
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onUrlChange(urlInput)
                        onBackClick()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LuxuryGold,
                        contentColor = LuxuryDeepBrown,
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        "SAVE SETTINGS",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                    )
                }
            }
        }
    }
}
