import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.internal.utils.getLocalProperty
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

val decomposeVersion = extra["decompose.version"] as String
val koinVersion = extra["koin.version"] as String
val ktorVersion = extra["ktor.version"] as String

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("kotlin-parcelize")
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig")
    id("io.realm.kotlin")
}

group = extra["app.group"] as String
version = extra["app.version"] as String

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
//                implementation(compose.materialIconsExtended)
                // Needed only for preview.
                implementation(compose.preview)
                // Decompose navigation library
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                // Koin-DI
                implementation("io.insert-koin:koin-core:$koinVersion")
                // Ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:1.2.7")
                // Date-time
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
                // METAR parser
                implementation("io.github.alexmaryin.metarkt:parser:1.0.1")
                // Realm
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
                implementation("io.realm.kotlin:library-base:1.10.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.12.0")
                // Koin DI
                implementation("io.insert-koin:koin-android:$koinVersion")

            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
            }
        }
    }
}

android {
    compileSdk = 34
    namespace = extra["app.group"] as String

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("resources")
//    sourceSets["main"].resources.srcDirs("resources")

    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

buildkonfig {
    packageName = "common"
    defaultConfigs {
        buildConfigField(STRING, "WXAPI_KEY", project.getLocalProperty("WXAPI_KEY"))
    }
}

