import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.internal.utils.getLocalProperty
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
            dependencies {
                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.components.resources)
//                implementation(libs.material.icons)
                implementation(libs.kotlinx.coroutines.core)
//                implementation(compose.materialIconsExtended)
                // Needed only for preview.
                implementation(compose.preview)
                // Decompose navigation library
                implementation(libs.decompose)
                implementation(libs.decompose.extensions)
                implementation(libs.decompose.essenity)
                // Serialization
                implementation(libs.kotlinx.serialization.json)
                // Koin-DI
                implementation(libs.koin)
                // Ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.client.serialization)
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
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.negotiation)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.activity.compose)
                api(libs.appcompat)
                api(libs.core.ktx)
                implementation(compose.foundation)
                implementation(libs.kotlinx.coroutines.android)
                // Koin DI
                implementation(libs.koin.android)
                // Room for version > 2.8.0
//                implementation(libs.androidx.room.sqlite.wrapper)

            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
                implementation(compose.desktop.common)
                implementation(compose.foundation)
            }
        }
    }
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        freeCompilerArgs.add("-Xwhen-guards")
    }
}

android {
    namespace = "ru.alexmaryin.simschecklist"
    compileSdk = 36

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

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
        buildConfigField(BOOLEAN, "DEBUG", "false")
        buildConfigField(STRING, "VER", libs.versions.mainVersion.get())
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
}
