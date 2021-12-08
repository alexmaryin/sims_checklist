plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

val decomposeVersion = "0.4.0"
//val koinVersion= "3.1.4"

android {
    compileSdkVersion(31)

    defaultConfig {
        minSdkVersion(22)
        targetSdkVersion(31)
        versionCode = 4
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.4.0")
    // Decompose navigation library
    implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation(kotlin("stdlib-jdk7", "1.5.31"))
    // Koin DI
//    implementation("io.insert-koin:koin-android:$koinVersion")
}
