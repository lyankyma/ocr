import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

// Module-level build.gradle.kts
plugins {
    id("com.android.application")
    id("kotlin-android")
    id "org.jetbrains.kotlin.android" version "1.9.0" // Example version


}


android {
    namespace = "com.example.ocr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ocr"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8" // Set a valid JVM target
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx.v1100)
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.mlkit:text-recognition:16.0.1")
    implementation("com.github.barteksc:pdfium-android:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.ui:ui:1.7.4") // Use the appropriate version
    implementation("androidx.compose.material3:material3:1.3.0") // Use the appropriate version
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.4")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.core:core:1.13.1") {
        exclude(group = "com.android.support", module = "support-compat")
    }









}
