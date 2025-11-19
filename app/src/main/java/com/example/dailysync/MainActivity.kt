// MainActivity.kt
package com.example.dailysync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.dailysync.presentation.DailySyncScreen
import dagger.hilt.android.AndroidEntryPoint

// ★ 追加: Hilt 用のアノテーション import(dagger.hilt.android.AndroidEntryPoint)
// ★ 追加: Hilt にこの Activity を認識させるためのアノテーション
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DailySyncApp()
        }
    }
}

@Composable
fun DailySyncApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            DailySyncScreen()
        }
    }
}
