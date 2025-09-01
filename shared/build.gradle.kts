import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.internal.utils.getLocalProperty

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    androidTarget()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            resources.srcDirs("resources")
            dependencies {
                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(libs.material.icons)
//                implementation(compose.materialIconsExtended)
                // Needed only for preview.
                implementation(compose.preview)
                // Decompose navigation library
                implementation(libs.decompose)
                implementation(libs.decompose.extensions)
                // Serialization
                implementation(libs.kotlinx.serialization.json)
                // Koin-DI
                implementation(libs.koin)
                // Ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
                // Date-time
                implementation(libs.kotlinx.datetime)
                // METAR parser
                implementation(libs.parser)
                // Room
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.activity.compose)
                api(libs.appcompat)
                api(libs.core.ktx)
                implementation(compose.foundation)
                // Koin DI
                implementation(libs.koin.android)
                // Room for version > 2.8.0
//                implementation(libs.androidx.room.sqlite.wrapper)

            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.foundation)
            }
        }
    }
}

android {
    namespace = "ru.alexmaryin.simschecklist"
    compileSdk = 35

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("resources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

buildkonfig {
    packageName = "common"
    defaultConfigs {
        buildConfigField(STRING, "WXAPI_KEY", project.getLocalProperty("WXAPI_KEY"))
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
}


