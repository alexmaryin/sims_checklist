plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

val decomposeVersion = extra["decompose.version"] as String

android {
    namespace = extra["app.group"] as String

    compileSdk = 34

    kotlin {
        jvmToolchain(11)
    }

    defaultConfig {
        minSdk = 22
        targetSdk = 34
        versionCode = (extra["app.release"] as String).toInt()
        versionName = extra["app.version"] as String
    }

    buildTypes {
        getByName("release") {
//            isDebuggable = true
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.material:material-icons-core:1.5.0")
    // Decompose navigation library
    implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation(kotlin("stdlib-jdk7", "1.8.0"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}
