// app/build.gradle.kts
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // ★ 追加: Spotless プラグインをモジュール側でも適用
    //        （version はルート build.gradle.kts 側で apply false している前提）
    id("com.diffplug.spotless")
    // ★ 追加: Hilt 用プラグイン
    id("com.google.dagger.hilt.android")
    // ★ 追加: Room / Hilt のコンパイラ用（kapt）
    kotlin("kapt")
}

android {
    namespace = "com.example.dailysync"
    compileSdk {
        version = release(36) // ← ここは元のまま。環境に合わせてOKならこのままで大丈夫
    }
    defaultConfig {
        applicationId = "com.example.dailysync"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // ★ ここは今のままでも動きますが、将来的には 17 も検討候補
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    // ★ （必要なら）Compose Compiler のバージョン指定などをここに追加してもよい
}

// ★ 追加: 新しい compilerOptions DSL を使った jvmTarget 指定（既存のまま利用）
kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    // --- 既存の依存関係（Compose / 基本ライブラリ） ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // --- Compose から viewModel() を使うための依存 ---
    // DailySyncScreen で `viewModel()` を使うため
    // implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // ★ 変更: version catalog 経由に

    // --- Room（DB 永続化） ---
    // implementation("androidx.room:room-runtime:2.8.4")
    // implementation("androidx.room:room-ktx:2.8.4")
    // kapt("androidx.room:room-compiler:2.8.4")
    implementation(libs.androidx.room.runtime)
    // ★ 変更: runtime を catalog から取得
    implementation(libs.androidx.room.ktx)
    // ★ 変更: ktx を catalog から取得
    kapt(libs.androidx.room.compiler)
    // ★ 変更: compiler を catalog から取得

    // --- Hilt（DI） ---
    // implementation("com.google.dagger:hilt-android:2.57.2")
    // kapt("com.google.dagger:hilt-android-compiler:2.57.2")
    implementation(libs.hilt.android)
    // ★ 変更: Hilt 本体を catalog から取得
    kapt(libs.hilt.android.compiler)
    // ★ 変更: Hilt コンパイラを catalog から取得

    // --- Hilt × Compose の連携（NavやViewModel用） ---
    // implementation("androidx.hilt:hilt-navigation-compose:1.3.0")
    implementation(libs.androidx.hilt.navigation.compose)
    // ★ 変更: catalog 経由に
}

// ★ 追加: Hilt + kapt のエラーを多少マイルドにする設定（任意）
kapt {
    correctErrorTypes = true
}

// ★ 追加: Spotless 設定ブロック
spotless {
    // Kotlin ソース（main / test 含めて全 .kt）
    kotlin {
        target("**/*.kt") // ★ 変更したくなったら後で app 配下に絞ることも可能
        ktlint().editorConfigOverride(
            mapOf(
                // ★ 必要に応じてルール微調整（とりあえず“うるさ過ぎない”ライン）
                "ktlint_standard_no-wildcard-imports" to "disabled",
                "ktlint_standard_max-line-length" to "disabled"
            )
        )
    }

    // Kotlin Gradle スクリプト（.gradle.kts）
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
    }

    // XML（レイアウト / Manifest / その他）
    format("xml") {
        target("**/*.xml")
        trimTrailingWhitespace()
        endWithNewline()
    }
}
