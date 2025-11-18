// app/build.gradle(.kts)
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // ★ 追加: Hilt 用プラグイン
    id("com.google.dagger.hilt.android") version "2.57.1" apply false

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

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    // ★ （必要なら）Compose Compiler のバージョン指定などをここに追加してもよい
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

    // --- ★ 追加: Compose から viewModel() を使うための依存 ---
    // DailySyncScreen で `viewModel()` を使うため
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")

    // --- ★ 追加: Room（DB 永続化） ---
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // --- ★ 追加: Hilt（DI） ---
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-android-compiler:2.52")

    // --- ★ 追加: Hilt × Compose の連携（NavやViewModel用） ---
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
}

// ★ 追加: Hilt + kapt のエラーを多少マイルドにする設定（任意）
kapt {
    correctErrorTypes = true
}
