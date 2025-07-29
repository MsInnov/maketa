plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("dagger.hilt.android.plugin")
    kotlin("plugin.compose")
    id("kotlin-kapt")
}

android {
    namespace = "com.mscode.presentation"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.material)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.coil.compose)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)

    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.material3)
    implementation(libs.androidx.core.splashscreen)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}