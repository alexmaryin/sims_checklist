import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.internal.utils.getLocalProperty
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
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
    android {
        namespace = "ru.alexmaryin.simschecklist.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }

        androidResources { enable = true }
    }

    jvm("desktop")

    sourceSets {
        val commonMain = getByName("commonMain") {
            dependencies {
                implementation(libs.compose.ui)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.components.resources)
                implementation(libs.material3.adaptive)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.compose.preview)
                implementation(libs.decompose)
                implementation(libs.decompose.extensions)
                implementation(libs.decompose.essenity)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.koin)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
                implementation(libs.kotlinx.datetime)
                implementation(libs.parser)
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
            }
        }
        val commonTest = getByName("commonTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.mock)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.negotiation)
            }
        }
        val androidMain = getByName("androidMain") {
            dependencies {
                api(libs.activity.compose)
                api(libs.appcompat)
                api(libs.core.ktx)
                implementation(libs.compose.foundation)
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.koin.android)
            }
        }
        val desktopMain = getByName("desktopMain") {
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.compose.desktop)
                implementation(libs.compose.foundation)
            }
        }
    }
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        freeCompilerArgs.add("-Xwhen-guards")
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
    "androidRuntimeClasspath"(libs.compose.tooling)
}

buildkonfig {
    packageName = "common"
    defaultConfigs {
        buildConfigField(STRING, "WXAPI_KEY", project.getLocalProperty("WXAPI_KEY"))
        buildConfigField(BOOLEAN, "DEBUG", "false")
        buildConfigField(STRING, "VER", libs.versions.mainVersion.get())
    }
}
