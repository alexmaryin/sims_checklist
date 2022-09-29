plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

val decomposeVersion = "0.4.0"

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 22
        targetSdk = 33
        versionCode = 6
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.5.1")
    // Decompose navigation library
    implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation(kotlin("stdlib-jdk7", "1.7.10"))
}
