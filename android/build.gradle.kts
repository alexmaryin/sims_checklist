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