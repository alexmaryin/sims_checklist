plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

group = "ru.alexmaryin.simschecklists"
version = "1.0.0"

val decomposeVersion = "0.4.0"

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "ru.alexmaryin.simschecklists.android"
        minSdk = 22
        targetSdk = 33
        versionCode = 8
        versionName = "1.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.7.0")
    // Decompose navigation library
    implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation(kotlin("stdlib-jdk7", "1.7.10"))
}
