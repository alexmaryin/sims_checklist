import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.internal.utils.getLocalProperty

val decomposeVersion = "2.0.1"
val koinVersion = "3.1.4"
val ktorVersion = "1.6.6"

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("kotlin-parcelize")
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig")
    id("io.realm.kotlin")
}

group = "ru.alexmaryin.simschecklist"
version = "1.0.3"

kotlin {
    androidTarget()
    jvm("desktop") {
        jvmToolchain(11)
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    sourceSets {
        val commonMain by getting {
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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
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
                implementation(kotlin("test")) // This brings all the platform dependencies automatically
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")
                // Koin DI
                implementation("io.insert-koin:koin-android:$koinVersion")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
    }
}

android {
    namespace = "ru.alexmaryin.simschecklist"
    compileSdk = 33

    defaultConfig {
        minSdk = 22
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

buildkonfig {
    packageName = "common"
    defaultConfigs {
        buildConfigField(STRING, "WXAPI_KEY", project.getLocalProperty("WXAPI_KEY"))
    }
}

