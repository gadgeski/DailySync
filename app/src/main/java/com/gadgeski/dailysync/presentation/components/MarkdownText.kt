package com.gadgeski.dailysync.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import com.gadgeski.dailysync.ui.theme.LuxuryGold
import com.gadgeski.dailysync.ui.theme.LuxuryTextPrimary

// ★ Improve: 正規表現は再コンポジションごとに生成せず、定数として保持する（パフォーマンス向上）
private val BOLD_REGEX = Regex("\\*\\*(.*?)\\*\\*")
private val ITALIC_REGEX = Regex("\\*(.*?)\\*")
private val ISSUE_REGEX = Regex("(#\\d+|[A-Z]+-\\d+)")

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = LuxuryTextPrimary,
    fontSize: TextUnit = TextUnit.Unspecified,
    issueTrackerUrlBase: String = "",
) {
    // ★ Improve: スタイル定義も remember でキャッシュし、再生成を防ぐ
    val linkStyle = remember {
        SpanStyle(
            color = LuxuryGold,
            textDecoration = TextDecoration.Underline,
        )
    }

    val annotatedString = remember(markdown, issueTrackerUrlBase) {
        buildAnnotatedString {
            append(markdown)

            // Apply styles for **Bold**
            BOLD_REGEX.findAll(markdown).forEach { matchResult ->
                addStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold),
                    start = matchResult.range.first,
                    end = matchResult.range.last + 1,
                )
            }

            // Apply styles for *Italic*
            ITALIC_REGEX.findAll(markdown).forEach { matchResult ->
                // Avoid overlapping with bold (e.g. inside **...**)
                if (!markdown.substring(matchResult.range).startsWith("**")) {
                    addStyle(
                        style = SpanStyle(fontStyle = FontStyle.Italic),
                        start = matchResult.range.first,
                        end = matchResult.range.last + 1,
                    )
                }
            }

            // Apply Issue Links
            if (issueTrackerUrlBase.isNotBlank()) {
                ISSUE_REGEX.findAll(markdown).forEach { matchResult ->
                    val issueId = matchResult.value
                    val url = if (issueId.startsWith("#")) {
                        "$issueTrackerUrlBase${issueId.substring(1)}"
                    } else {
                        "$issueTrackerUrlBase$issueId"
                    }

                    addLink(
                        url = LinkAnnotation.Url(
                            url = url,
                            styles = TextLinkStyles(style = linkStyle),
                        ),
                        start = matchResult.range.first,
                        end = matchResult.range.last + 1,
                    )
                }
            }
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        style = style.copy(color = color, fontSize = fontSize),
    )
}
