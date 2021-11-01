import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

buildscript {
    val composeVersion = "1.0.0-beta5"

    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:$composeVersion")
        classpath("com.android.tools.build:gradle:4.2.2")
        classpath(kotlin("gradle-plugin", version = "1.5.31"))
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.18.1")
}

detekt {
    toolVersion = "1.18.1"
    config = files("config/detekt/detekt.yml")
    source = files("common/src", "android/src/main/java", "desktop/src/jvmMain")
    buildUponDefaultConfig = true
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

subprojects {
    afterEvaluate {
        project.extensions.findByType<KotlinMultiplatformExtension>()?.let { ext ->
            ext.sourceSets.removeAll { sourceSet ->
                setOf(
                    "androidAndroidTestRelease",
                    "androidTestFixtures",
                    "androidTestFixturesDebug",
                    "androidTestFixturesRelease",
                ).contains(sourceSet.name)
            }
        }
    }
}
