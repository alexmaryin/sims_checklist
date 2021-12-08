import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val decomposeVersion = "0.4.0"
val koinVersion= "3.1.4"
val ktorVersion = "1.6.6"

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "1.5.31"
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        named("commonMain") {
            resources.srcDirs("resources")
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
//                api(compose.materialIconsExtended)
                // Needed only for preview.
                implementation(compose.preview)
                // Decompose navigation library
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
                // Koin-DI
                implementation("io.insert-koin:koin-core:$koinVersion")
                // Ktor
                implementation ("io.ktor:ktor-client-core:$ktorVersion")
                implementation ("io.ktor:ktor-client-cio:$ktorVersion")
                implementation ("io.ktor:ktor-client-logging:$ktorVersion")
                implementation ("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
                implementation ("ch.qos.logback:logback-classic:1.2.7")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test")) // This brings all the platform dependencies automatically
            }
        }
        named("androidMain") {
            dependencies {
                api("androidx.appcompat:appcompat:1.4.0")
                api("androidx.core:core-ktx:1.7.0")
                // Koin DI
                implementation("io.insert-koin:koin-android:$koinVersion")
            }
        }
    }
}

android {
    compileSdkVersion(31)

    defaultConfig {
        minSdkVersion(22)
        targetSdkVersion(31)
//        versionCode = 4
//        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("resources")
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xopt-in=kotlin.RequiresOptIn"
    )
}

