plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
}

val decomposeVersion = extra["decompose.version"] as String

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.foundation)
                implementation(compose.material)
                implementation("androidx.activity:activity-compose:1.7.2")
                implementation("androidx.core:core-ktx:1.12.0")
                // Decompose
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
            }
        }
    }
}

android {
    compileSdk = 34
    namespace = extra["app.group"] as String

    defaultConfig {
        minSdk = 26
        targetSdk = 34
        applicationId = extra["app.group"] as String
        versionCode = (extra["app.release"] as String).toInt()
        versionName = extra["app.version"] as String
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}