// ui/theme/Type.kt

package com.example.dailysync.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ★ コンセプト: "Modern Luxury Editorial"
// 見出しには「セリフ体 (Serif)」を使い、高級感と知性を演出。
// 本文には「サンセリフ体 (SansSerif)」を使い、可読性とモダンさを確保。

val AppTypography = Typography(
    // ★ 巨大な日付表示用（雑誌の表紙風）
    displayLarge = TextStyle(
        fontFamily = FontFamily.Serif, // 明朝・セリフ系
        fontWeight = FontWeight.Normal, // 細めで上品に
        fontSize = 80.sp,
        lineHeight = 88.sp,
        letterSpacing = (-1.5).sp, // 詰め気味にして塊感を出す
    ),

    // ★ セクション見出し用（HISTORY / LOGS など）
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 2.sp, // 文字間を広げて高級感を出す
    ),

    // ★ 入力欄のタイトルなど
    titleMedium = TextStyle(
        fontFamily = FontFamily.Serif, // ここもセリフにして統一感を
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),

    // ★ 本文・ボタン文字など
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Light, // 繊細に
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
)
