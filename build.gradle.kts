// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.google.services)
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    kotlin("plugin.compose") version "2.0.0" apply false
}