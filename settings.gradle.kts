pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform").version(extra["kotlin.version"] as String)
        id("org.gradle.toolchains.foojay-resolver-convention") version("0.5.0")
        kotlin("android").version(extra["kotlin.version"] as String)
        id("com.android.application").version(extra["agp.version"] as String)
        id("com.android.library").version(extra["agp.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
        kotlin("plugin.serialization").version(extra["serialization.version"] as String)
        id("com.codingfeline.buildkonfig").version(extra["buildkonfig.version"] as String)
        id("io.realm.kotlin").version(extra["realm.version"] as String)
    }
}

rootProject.name = "sims_checklist"

include(":common", ":android", ":desktop")
