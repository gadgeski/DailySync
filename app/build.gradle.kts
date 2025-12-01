// app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // ★ Fix: HiltプラグインをCatalog経由で適用
    alias(libs.plugins.hilt.android)
    // ★ Fix: kapt ではなく KSP プラグインを使用（高速化・推奨）
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.dailysync"
    compileSdk = 36

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
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    // ★ Fix: lifecycle-runtime-compose に含まれるため削除 (Unresolved reference エラー解消)
    // implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Activity & Navigation
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // Material (View System for themes.xml)
    implementation(libs.material)
    // ★ Added: これで Unresolved reference 'material' が消えます

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Appcompat
    implementation(libs.androidx.appcompat)

    // Paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // Room (KSP対応)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    // ★ Fix: kapt -> ksp に変更
    ksp(libs.androidx.room.compiler)

    // Hilt (KSP対応)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    // ★ Fix: kapt -> ksp に変更
    ksp(libs.hilt.compiler)

    // LeakCanary (Debug only)
    debugImplementation(libs.leakcanary.android)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
