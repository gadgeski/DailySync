// build.gradle.kts（ルート）
// Top-level build file where you can add configuration options common to all sub-projects/modules.

import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // ★ Fix: Version Catalog (libs.versions.toml) で定義したプラグインを適用
    // 定義名が [plugins] hilt-android の場合、ここは libs.plugins.hilt.android になります
    alias(libs.plugins.hilt.android) apply false

    // ★ Fix: KSP プラグイン (kapt の代替)
    alias(libs.plugins.ksp) apply false

    // ★ Fix: Spotless プラグイン
    alias(libs.plugins.spotless) apply false
}

// すべてのサブプロジェクト（= :app）に Spotless を適用
subprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<SpotlessExtension> {
        // Kotlin ソース
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**", "**/generated/**")

            ktlint().editorConfigOverride(
                mapOf(
                    // Composable の命名規則緩和
                    "ktlint_function_naming_ignore_when_annotated_with" to "Composable,Preview",
                    // ユーザー設定（必要に応じて有効化）
                    "ktlint_standard_no-wildcard-imports" to "disabled",
                    "ktlint_standard_max-line-length" to "disabled"
                )
            )
        }

        // Kotlin Gradle スクリプト
        kotlinGradle {
            target("**/*.gradle.kts")
            targetExclude("**/build/**")
            ktlint()
        }

        // XML
        format("xml") {
            target("**/*.xml")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}