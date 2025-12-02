// MainActivity.kt
@file:Suppress("ktlint:standard:function-naming")

package com.example.dailysync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dailysync.presentation.DailySyncScreen
import com.example.dailysync.presentation.DailySyncViewModel
import com.example.dailysync.presentation.SettingsScreen
import com.example.dailysync.ui.theme.DailySyncTheme
import dagger.hilt.android.AndroidEntryPoint
import android.graphics.Color as AndroidColor

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                AndroidColor.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.dark(
                AndroidColor.TRANSPARENT,
            ),
        )

        setContent {
            DailySyncTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent,
                ) {
                    DailySyncApp()
                }
            }
        }
    }
}

// 画面遷移のルート定義
object Routes {
    const val HOME = "home"
    const val SETTINGS = "settings"
}

@Composable
fun DailySyncApp() {
    // ★ Improve: Navigation Compose を導入して画面遷移を管理
    val navController = rememberNavController()

    // ViewModelをここで取得し、各画面で共有する（簡易的なスコープ管理）
    // ※ 本格的なアプリでは各画面で hiltViewModel() を呼ぶか、NavGraphスコープを使いますが、
    //    今回は既存のデータフロー（MainActivityでVMを持つ形）を維持して移行します。
    val viewModel: DailySyncViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
    ) {
        // メイン画面（日報一覧）
        composable(Routes.HOME) {
            DailySyncScreen(
                viewModel = viewModel,
                onSettingsClick = {
                    navController.navigate(Routes.SETTINGS)
                },
            )
        }

        // 設定画面
        composable(Routes.SETTINGS) {
            SettingsScreen(
                currentUrl = viewModel.issueTrackerUrlBase,
                onUrlChange = { viewModel.updateIssueTrackerUrlBase(it) },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
