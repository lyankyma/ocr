// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Project build.gradle.kts
buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }  // Required for PdfiumAndroid
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
    }
}



